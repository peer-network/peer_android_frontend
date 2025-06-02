package eu.peernetwork.app.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.app.usecase.BootstrapUsecase
import eu.peernetwork.app.usecase.VersionUseCase
import io.mockk.coEvery
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

    private val bootstrapUsecase = mockk<BootstrapUsecase>()

    private val versionUseCase = mockk<VersionUseCase>()

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(bootstrapUsecase, versionUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize state`() = runTest {
        coEvery { bootstrapUsecase() } coAnswers { delay(100) }
        coEvery { versionUseCase() } returns VersionUseCase.Result.UpToDate
        viewModel.initialize()
        viewModel.state.test {
            assertEquals(SplashViewModel.State.Loading, awaitItem())
            assertEquals(SplashViewModel.State.Success(), awaitItem())
        }
    }

    @Test
    fun `test initialize state error`() = runTest {
        val url = "<test-url>"
        coEvery { versionUseCase() } returns VersionUseCase.Result.Outdated(url)
        viewModel.initialize()
        viewModel.state.test {
            assertEquals(SplashViewModel.State.Success(url), awaitItem())
        }
    }
}
