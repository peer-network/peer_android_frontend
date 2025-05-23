package eu.peernetwork.user.ui.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.AuthUserUsecase
import eu.peernetwork.user.domain.usecase.ProfileUsecase
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.UserUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<ProfileUsecase>()

    private val userUsecase = mockk<UserUsecase>()

    private val authUserUsecase = mockk<AuthUserUsecase>()

    private val observeAuthUserUsecase = mockk<ObserveAuthUserUsecase>()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserViewModel(usecase, userUsecase, authUserUsecase, observeAuthUserUsecase)
    }

    @Test
    fun `test get user success`() = runTest {
        val mockData = mockk<UiAccount>(relaxed = true)
        coEvery { usecase(any()) } returns mockk(relaxed = true)
        coEvery { observeAuthUserUsecase() } returns flowOf(mockData)
        coEvery { userUsecase(any()) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getAccount("<test-id>")
        viewModel.state.test {
            assertEquals(UserViewModel.State.Loading, awaitItem())
            assertEquals(UserViewModel.State.Success(mockData, true), awaitItem())
        }
    }

    @Test
    fun `test get guest user success`() = runTest {
        val guest = mockk<UiAccount>(relaxed = true)
        val mockData = mockk<UiAccount>(relaxed = true)
        every { guest.id } returns "<test-guest-id>"
        coEvery { usecase(any()) } returns mockk(relaxed = true)
        coEvery { observeAuthUserUsecase() } returns flowOf(guest)
        coEvery { userUsecase(any()) } returns mockData
        viewModel.getAccount("<test-id>")
        viewModel.state.test {
            assertEquals(UserViewModel.State.Success(mockData, false), awaitItem())
        }
    }

    @Test
    fun `test get user error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase(any()) } returns mockk(relaxed = true)
        coEvery { userUsecase(any()) } throws error
        viewModel.getAccount("<test-id>")
        viewModel.state.test {
            assertEquals(UserViewModel.State.Error(error), awaitItem())
        }
    }
}
