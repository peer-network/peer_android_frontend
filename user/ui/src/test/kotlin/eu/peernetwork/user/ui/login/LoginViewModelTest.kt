package eu.peernetwork.user.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.LoginUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val loginUsecase = mockk<LoginUsecase>()

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = LoginViewModel(loginUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test login success`() = runTest {
        val token = "<test-token>"
        coEvery { loginUsecase(any()) } coAnswers {
            delay(100)
            token
        }
        viewModel.login("<test-email>", "<test-password>")
        viewModel.state.test {
            assertEquals(LoginViewModel.State.Loading, awaitItem())
            assertEquals(LoginViewModel.State.Success(token), awaitItem())
        }
        coVerify { loginUsecase(LoginUsecase.Parameter("<test-email>", "<test-password>")) }
    }

    @Test
    fun `test login error`() = runTest {
        val error = RuntimeException("<test-login-error>")
        coEvery { loginUsecase(any()) } throws error

        viewModel.login("<test-email>", "<test-password>")
        viewModel.state.test {
            assertEquals(LoginViewModel.State.Error(error), awaitItem())
        }
    }
}
