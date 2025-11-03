package eu.peernetwork.user.ui.settings.account

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.DeactivationUsecase
import eu.peernetwork.user.domain.usecase.LogoutUsecase
import eu.peernetwork.user.domain.usecase.ProtectedSettingsUsecase
import eu.peernetwork.user.domain.usecase.SettingsUsecase
import eu.peernetwork.user.ui.account.AccountViewModel
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiSettings
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.ProfileRefreshUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AccountViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val user = MutableStateFlow<UiAccount?>(null)

    private val dispatcher = UnconfinedTestDispatcher()

    private val profileRefreshUsecase = mockk<ProfileRefreshUsecase>()

    private val settingsUsecase = mockk<SettingsUsecase>()

    private val protectedSettingsUsecase = mockk<ProtectedSettingsUsecase>()

    private val observeAuthUserUsecase = mockk<ObserveAuthUserUsecase>()

    private val logoutUsecase = mockk<LogoutUsecase>()

    private val deactivationUsecase = mockk<DeactivationUsecase>()

    private lateinit var viewModel: AccountViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        every { observeAuthUserUsecase() } returns user

        viewModel = AccountViewModel(
            profileRefreshUsecase,
            settingsUsecase,
            protectedSettingsUsecase,
            observeAuthUserUsecase,
            logoutUsecase,
            deactivationUsecase
        )
    }

    @Test
    fun `test update protected user detail success`() = runTest {
        val account = mockk<UiAccount>(relaxed = true)
        val model = mockk<UiSettings>(relaxed = true)
        val password = "<test-password>"

        coEvery { model.protected } returns true
        coEvery { protectedSettingsUsecase(any()) } returns Unit
        coEvery { profileRefreshUsecase() } coAnswers {
            delay(100)
            user.tryEmit(account)
            account
        }

        viewModel.update(account, listOf(model), password)
        viewModel.state.test {
            assertEquals(AccountViewModel.State.Loading, awaitItem())
            assertEquals(AccountViewModel.State.Success(account, false), awaitItem())
        }
    }

    @Test
    fun `test update protected user detail error`() = runTest {
        val error = RuntimeException()
        val account = mockk<UiAccount>(relaxed = true)
        val model = mockk<UiSettings>(relaxed = true)
        val password = "<test-password>"

        coEvery { settingsUsecase(any()) } coAnswers {
            delay(100)
            user.tryEmit(account)
            throw error
        }
        viewModel.update(account, listOf(model), password)
        viewModel.state.test {
            assertEquals(AccountViewModel.State.Loading, awaitItem())
            assertEquals(
                AccountViewModel.State.Success(account, false, error),
                awaitItem()
            )
        }
    }

    @Test
    fun `test update un protected user detail success`() = runTest {
        val account = mockk<UiAccount>(relaxed = true)
        val model = mockk<UiSettings>(relaxed = true)
        val password = "<test-password>"

        coEvery { settingsUsecase(any()) } returns Unit
        coEvery { profileRefreshUsecase() } coAnswers {
            delay(100)
            user.tryEmit(account)
            account
        }

        viewModel.update(account, listOf(model), password)
        viewModel.state.test {
            assertEquals(AccountViewModel.State.Loading, awaitItem())
            assertEquals(AccountViewModel.State.Success(account, false), awaitItem())
        }
    }

    @Test
    fun `test logout user success`() = runTest {
        coEvery { logoutUsecase() } coAnswers {
            delay(100)
            user.tryEmit(null)
        }
        viewModel.logout()
        viewModel.state.test {
            assertEquals(AccountViewModel.State.Loading, awaitItem())
            assertEquals(AccountViewModel.State.Default, awaitItem())
        }
        coVerify { logoutUsecase() }
    }

    @Test
    fun `test deactivate user success`() = runTest {
        val password = "<test-password>"
        coEvery { deactivationUsecase(any()) } returns Unit
        coEvery { logoutUsecase() } coAnswers {
            delay(100)
            user.tryEmit(null)
        }
        viewModel.deactivate(password)
        viewModel.state.test {
            assertEquals(AccountViewModel.State.Loading, awaitItem())
            assertEquals(AccountViewModel.State.Default, awaitItem())
        }
        coVerify { deactivationUsecase(password) }
    }
}