package eu.peernetwork.user.ui.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.core.common.provider.DispatcherProvider
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.usecase.AuthenticatedUserUsecase
import eu.peernetwork.user.domain.usecase.DescriptionUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.user.core.UserViewModel
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
internal class UserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<AuthenticatedUserUsecase>()

    private val descriptionUsecase = mockk<DescriptionUsecase>()

    private val dispatcherProvider = mockk<DispatcherProvider>()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { dispatcherProvider.io } returns dispatcher

        viewModel = UserViewModel(dispatcherProvider, usecase, descriptionUsecase)
    }

    @Test
    fun `test get authenticated user success`() = runTest {
        val description = "<test-description>"
        val mockData = mockk<Account>(relaxed = true)
        val uiModel = mockData.mapFromDomain()
        coEvery { descriptionUsecase(any()) } returns description
        coEvery { usecase() } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getAccount()
        viewModel.state.test {
            assertEquals(UserViewModel.State.Loading, awaitItem())
            assertEquals(UserViewModel.State.Success(uiModel.copy(bio = description)), awaitItem())
        }
    }

    @Test
    fun `test get authenticated user description error`() = runTest {
        val mockData = mockk<Account>(relaxed = true)
        val uiModel = mockData.mapFromDomain()
        coEvery { descriptionUsecase(any()) } throws RuntimeException()
        coEvery { usecase() } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getAccount()
        viewModel.state.test {
            assertEquals(UserViewModel.State.Loading, awaitItem())
            assertEquals(UserViewModel.State.Success(uiModel.copy(bio = null)), awaitItem())
        }
    }

    @Test
    fun `test get authenticated user error`() = runTest {
        val mockData = RuntimeException()
        coEvery { usecase() } throws mockData
        viewModel.getAccount()
        viewModel.state.test {
            assertEquals(UserViewModel.State.Error(mockData), awaitItem())
        }
    }
}