package eu.peernetwork.user.ui.settings.address

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.ProtectedSettingsUsecase
import eu.peernetwork.user.ui.email.EmailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddressViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<ProtectedSettingsUsecase>()

    private lateinit var viewModel: EmailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = EmailViewModel(usecase)
    }

    @Test
    fun `test update protected user detail success`() = runTest {
        val email = "<test-email>"
        val password = "<test-password>"

        coEvery { usecase(any()) } coAnswers { delay(100) }

        viewModel.update(email, password)
        viewModel.state.test {
            assertEquals(EmailViewModel.State.Loading, awaitItem())
            assertEquals(EmailViewModel.State.Success(email), awaitItem())
        }
    }

    @Test
    fun `test update protected user detail error`() = runTest {
        val email = "<test-email>"
        val password = "<test-password>"
        val error = RuntimeException()

        coEvery { usecase(any()) } throws error

        viewModel.update(email, password)
        viewModel.state.test {
            assertEquals(EmailViewModel.State.Error(error), awaitItem())
        }
    }
}
