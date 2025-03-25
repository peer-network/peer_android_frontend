package eu.peernetwork.user.ui.user.point

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.PointUsecase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserPointViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<PointUsecase>()

    private lateinit var viewModel: UserPointViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserPointViewModel(usecase)
    }

    @Test
    fun `test get authenticated user success`() = runTest {
        coEvery { usecase() } coAnswers {
            delay(100)
            listOf()
        }
        viewModel.getPoints()
        viewModel.state.test {
            assertEquals(UserPointViewModel.State.Loading, awaitItem())
            assertEquals(UserPointViewModel.State.Success(listOf()), awaitItem())
        }
    }

    @Test
    fun `test get authenticated user error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase() } coAnswers {
            delay(100)
            throw error
        }
        viewModel.getPoints()
        viewModel.state.test {
            assertEquals(UserPointViewModel.State.Loading, awaitItem())
            assertEquals(UserPointViewModel.State.Error(error), awaitItem())
        }
    }
}
