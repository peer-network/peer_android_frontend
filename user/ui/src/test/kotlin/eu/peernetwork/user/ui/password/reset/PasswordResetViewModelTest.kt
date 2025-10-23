package eu.peernetwork.user.ui.password.reset

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.PasswordResetUsecase
import eu.peernetwork.user.ui.v2.password.reset.ResetViewModel
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
internal class PasswordResetViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<PasswordResetUsecase>()

    private lateinit var viewModel: ResetViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ResetViewModel(usecase)
    }

    @Test
    fun `test reset password success`() = runTest {
        val token = "<test-token>"
        val password = "<test-password>"
        coEvery { usecase(any()) } coAnswers {
            delay(100)
        }
        viewModel.reset(token, password)
        viewModel.state.test {
            assertEquals(ResetViewModel.State.Loading, awaitItem())
            assertEquals(ResetViewModel.State.Success, awaitItem())
        }
    }

    @Test
    fun `test reset password error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase(any()) } throws error
        viewModel.reset("<test-token>", "<test-email>")
        viewModel.state.test {
            assertEquals(ResetViewModel.State.Error(error), awaitItem())
        }
    }
}
