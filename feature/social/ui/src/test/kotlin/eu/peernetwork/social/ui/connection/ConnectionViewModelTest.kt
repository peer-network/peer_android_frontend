package eu.peernetwork.social.ui.connection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.social.domain.usecase.ConnectionsUsecase
import eu.peernetwork.social.domain.usecase.ObserveConnectionsUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConnectionViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<ConnectionsUsecase>()

    private val observerUsecase = mockk<ObserveConnectionsUsecase>()

    private lateinit var viewModel: ConnectionViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ConnectionViewModel(usecase, observerUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialization`() = runTest {
        val connections = mapOf("<test-id>" to false)
        every { observerUsecase() } returns flowOf(connections)
        viewModel.initialize()
        viewModel.connections.test {
            assertEquals(connections, awaitItem())
        }
    }

    @Test
    fun `test connection success`() = runTest {
        val connections = Pair("<test-id>", false)
        coEvery { usecase(any()) } coAnswers {
            delay(100)
        }
        viewModel.connect(connections.first, connections.second)
        viewModel.state.test {
            assertEquals(ConnectionViewModel.State.Loading, awaitItem())
            assertEquals(ConnectionViewModel.State.Success, awaitItem())
        }
    }

    @Test
    fun `test connection error`() = runTest {
        val error = RuntimeException()
        val connections = Pair("<test-id>", false)
        coEvery { usecase(any()) } throws error
        viewModel.connect(connections.first, connections.second)
        viewModel.state.test {
            assertEquals(ConnectionViewModel.State.Error(error), awaitItem())
        }
    }

    @Test
    fun `test reset`() = runTest {
        val connections = Pair("<test-id>", false)
        coEvery { usecase(any()) } returns Unit
        viewModel.connect(connections.first, connections.second)
        viewModel.reset()
        viewModel.state.test {
            assertEquals(ConnectionViewModel.State.Empty, awaitItem())
        }
    }
}
