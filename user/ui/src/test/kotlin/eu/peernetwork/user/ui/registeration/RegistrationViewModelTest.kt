package eu.peernetwork.user.ui.registeration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.RegistrationUsecase
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
internal class RegistrationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<RegistrationUsecase>()

    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = RegistrationViewModel(usecase)
    }

    @Test
    fun `test registration success`() = runTest {
        val uuid = "<test-uuid>"
        coEvery { usecase(any()) } coAnswers {
            delay(100)
            uuid
        }
        viewModel.register("<test-username>", "<test-email>", "<test-password>")
        viewModel.state.test {
            assertEquals(RegistrationViewModel.State.Loading, awaitItem())
            assertEquals(RegistrationViewModel.State.Success(uuid), awaitItem())
        }
    }

    @Test
    fun `test registration error`() = runTest {
        val error = RuntimeException("<test-uuid>")
        coEvery { usecase(any()) } throws error
        viewModel.register("<test-username>", "<test-email>", "<test-password>")
        viewModel.state.test {
            assertEquals(RegistrationViewModel.State.Error(error), awaitItem())
        }
    }
}