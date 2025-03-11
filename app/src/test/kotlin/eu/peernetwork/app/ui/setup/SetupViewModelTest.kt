package eu.peernetwork.app.ui.setup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.observable.ObservableBoolean
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetupViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val flow = MutableSharedFlow<Boolean?>(replay = 1)

    private val observableBoolean = mockk<ObservableBoolean>()

    private val publishableBoolean = mockk<PublishableBoolean>()

    private lateinit var viewModel: SetupViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        every { observableBoolean(any()) } returns flow
        coEvery { publishableBoolean(any(), any()) } answers {
            flow.tryEmit(secondArg<Boolean>())
        }

        viewModel = SetupViewModel(observableBoolean, publishableBoolean)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test login state`() = runTest {
        flow.tryEmit(false)
        viewModel.state.test {
            assertEquals(SetupViewModel.State.Login, awaitItem())
        }
    }

    @Test
    fun `test registration state`() = runTest {
        flow.tryEmit(true)
        viewModel.state.test {
            assertEquals(SetupViewModel.State.Register, awaitItem())
        }
    }
}
