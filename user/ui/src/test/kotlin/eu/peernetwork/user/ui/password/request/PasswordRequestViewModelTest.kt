package eu.peernetwork.user.ui.password.request

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.PasswordRequestUsecase
import eu.peernetwork.user.ui.usecase.EmailMaskUsecase
import eu.peernetwork.user.ui.v2.password.request.RequestViewModel
import io.mockk.coEvery
import io.mockk.every
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
internal class PasswordRequestViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<PasswordRequestUsecase>()

    private val emailMaskUsecase = mockk<EmailMaskUsecase>()

    private lateinit var viewModel: RequestViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = RequestViewModel(usecase, emailMaskUsecase)
    }

    @Test
    fun `test request password success`() = runTest {
        val email = "<test-email>"
        every { emailMaskUsecase(any()) } returns email
        coEvery { usecase(any()) } coAnswers {
            delay(100)
        }
        viewModel.requestPassword(email)
        viewModel.state.test {
            assertEquals(RequestViewModel.State.Loading, awaitItem())
            assertEquals(RequestViewModel.State.Success(email), awaitItem())
        }
    }

    @Test
    fun `test request password error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase(any()) } throws error
        viewModel.requestPassword("<test-email>")
        viewModel.state.test {
            assertEquals(RequestViewModel.State.Error(error), awaitItem())
        }
    }
}
