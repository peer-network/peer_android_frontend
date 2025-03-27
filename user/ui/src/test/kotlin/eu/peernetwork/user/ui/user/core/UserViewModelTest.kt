package eu.peernetwork.user.ui.user.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.core.common.provider.DispatcherProvider
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.ProfileUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val user = MutableStateFlow<UiAccount?>(null)

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<ProfileUsecase>()

    private val observer = mockk<ObserveAuthUserUsecase>()

    private val dispatcherProvider = mockk<DispatcherProvider>()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { observer() } returns user
        every { dispatcherProvider.io } returns dispatcher

        viewModel = UserViewModel(usecase, observer)
    }

    @Test
    fun `test get authenticated user success`() = runTest {
        val mockData = mockk<UiAccount>(relaxed = true)
        coEvery { usecase() } coAnswers {
            delay(100)
            user.tryEmit(mockData)
            mockData
        }
        viewModel.getAccount()
        viewModel.state.test {
            TestCase.assertEquals(UserViewModel.State.Loading, awaitItem())
            TestCase.assertEquals(UserViewModel.State.Success(mockData), awaitItem())
        }
    }

    @Test
    fun `test get authenticated user error`() = runTest {
        val mockData = RuntimeException()
        coEvery { usecase() } throws mockData
        viewModel.getAccount()
        viewModel.state.test {
            TestCase.assertEquals(UserViewModel.State.Error(mockData), awaitItem())
        }
    }
}