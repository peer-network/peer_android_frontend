package eu.peernetwork.user.ui.password.update

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.usecase.PasswordChangeUsecase
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
internal class PasswordUpdateViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<PasswordChangeUsecase>()

    private lateinit var viewModel: PasswordUpdateViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = PasswordUpdateViewModel(usecase)
    }

    @Test
    fun `test update protected user detail success`() = runTest {
        val new = "<test-new-password>"
        val current = "<test-current-password>"

        coEvery { usecase(any()) } coAnswers { delay(100) }

        viewModel.update(current, new)
        viewModel.state.test {
            assertEquals(PasswordUpdateViewModel.State.Loading, awaitItem())
            assertEquals(PasswordUpdateViewModel.State.Success, awaitItem())
        }
    }

    @Test
    fun `test update protected user detail error`() = runTest {
        val new = "<test-new-password>"
        val current = "<test-current-password>"
        val error = RuntimeException()

        coEvery { usecase(any()) } throws error

        viewModel.update(current, new)
        viewModel.state.test {
            assertEquals(PasswordUpdateViewModel.State.Error(error), awaitItem())
        }
    }
}
