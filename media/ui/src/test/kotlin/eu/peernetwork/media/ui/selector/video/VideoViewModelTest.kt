package eu.peernetwork.media.ui.selector.video

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.ui.usecase.VideoUsecase
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
internal class VideoViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<VideoUsecase>()

    private lateinit var viewModel: VideoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = VideoViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize success`() = runTest {
        val thumbnail = "<test-thumbnail>"
        val file = UiFile(mockk(), thumbnail)
        val videos = listOf(file)
        coEvery { usecase(any()) } coAnswers {
            delay(100)
            videos
        }
        viewModel.initialize(null)
        viewModel.state.test {
            assertEquals(VideoViewModel.State.Loading, awaitItem())
            assertEquals(VideoViewModel.State.Success(videos), awaitItem())
        }
    }

    @Test
    fun `test initialize error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase(any()) } throws error
        viewModel.initialize(null)
        viewModel.state.test {
            assertEquals(VideoViewModel.State.Error(error), awaitItem())
        }
    }
}
