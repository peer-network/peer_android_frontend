package eu.peernetwork.app.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.app.usecase.LogDeviceModelUsecase
import eu.peernetwork.app.usecase.VersionUseCase
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
internal class SplashViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val versionUseCase = mockk<VersionUseCase>()
    private val logDeviceModelUsecase = mockk<LogDeviceModelUsecase>()

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(versionUseCase, logDeviceModelUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize state - UpToDate`() = runTest {
        coEvery { logDeviceModelUsecase() } returns Unit
        coEvery { versionUseCase() } coAnswers {
            delay(100)
        }

        viewModel.initialize()

        viewModel.state.test {
            assertEquals(SplashViewModel.State.Loading, awaitItem())
            assertEquals(SplashViewModel.State.Success, awaitItem())
        }

        coVerify(exactly = 1) { logDeviceModelUsecase() }
        coVerify(exactly = 1) { versionUseCase() }
    }

    @Test
    fun `test initialize state - Outdated`() = runTest {
        val url = "<test-url>"

        coEvery { logDeviceModelUsecase() } returns Unit
        coEvery { versionUseCase() } returns Unit

        viewModel.initialize()

        viewModel.state.test {
            assertEquals(SplashViewModel.State.Success, awaitItem())
        }

        coVerify(exactly = 1) { logDeviceModelUsecase() }
        coVerify(exactly = 1) { versionUseCase() }
    }


    @Test
    fun `test initialize state - VersionUseCase error`() = runTest {
        val exception = Throwable("Simulated error")

        coEvery { logDeviceModelUsecase() } returns Unit
        coEvery { versionUseCase() } throws exception

        viewModel.initialize()

        viewModel.state.test {
            assertEquals(SplashViewModel.State.Error(exception), awaitItem())
        }

        coVerify(exactly = 1) { logDeviceModelUsecase() }
        coVerify(exactly = 1) { versionUseCase() }
    }

    @Test
    fun `test initialize state - LogDeviceModelUsecase success`() = runTest {
        coEvery { logDeviceModelUsecase() } returns Unit
        coEvery { versionUseCase() } returns Unit

        viewModel.initialize()

        viewModel.state.test {
            assertEquals(SplashViewModel.State.Success, awaitItem())
        }

        coVerify(exactly = 1) { logDeviceModelUsecase() }
        coVerify(exactly = 1) { versionUseCase() }
    }
    
    @Test
    fun `test initialize state - LogDeviceModelUsecase throws error`() = runTest {
        val exception = Throwable("Device log failed")

        coEvery { logDeviceModelUsecase() } throws exception

        viewModel.initialize()

        viewModel.state.test {
            assertEquals(SplashViewModel.State.Error(exception), awaitItem())
        }

        coVerify(exactly = 1) { logDeviceModelUsecase() }
        coVerify(exactly = 0) { versionUseCase() }
    }
}
