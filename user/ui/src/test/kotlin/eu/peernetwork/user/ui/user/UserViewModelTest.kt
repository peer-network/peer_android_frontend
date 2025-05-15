package eu.peernetwork.user.ui.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
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

    private val observeAuthUserUsecase = mockk<ObserveAuthUserUsecase>()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserViewModel(usecase, userUsecase, observeAuthUserUsecase)
    }

    @Test
    fun `test get user success`() = runTest {
        val mockData = mockk<UiAccount>(relaxed = true)
        every { observeAuthUserUsecase() } returns flowOf(null)
        coEvery { usecase(any()) } returns mockk(relaxed = true)
        coEvery { userUsecase(any()) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getAccount("<test-id>")
        viewModel.state.test {
            assertEquals(UserViewModel.State.Loading, awaitItem())
            assertEquals(UserViewModel.State.Success(mockData, false), awaitItem())
        }
    }

    @Test
    fun `test get user error`() = runTest {
        val error = RuntimeException()
        every { observeAuthUserUsecase() } returns flowOf(null)
        coEvery { usecase(any()) } returns mockk(relaxed = true)
        coEvery { userUsecase(any()) } throws error
        viewModel.getAccount("<test-id>")
        viewModel.state.test {
            assertEquals(UserViewModel.State.Error(error), awaitItem())
        }
    }
}
