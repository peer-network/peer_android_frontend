package eu.peernetwork.media.ui.selector.directory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiDirectory
import eu.peernetwork.media.ui.usecase.PhotoDirectoryUsecase
import eu.peernetwork.media.ui.usecase.VideoDirectoryUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class DirectoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val usecase = mockk<PhotoDirectoryUsecase>()

    private val videoUsecase = mockk<VideoDirectoryUsecase>()

    private val thumbnailInteractor = mockk<ThumbnailInteractor>()

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: DirectoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { thumbnailInteractor.observe() } returns flowOf(emptyMap())
        viewModel = DirectoryViewModel(usecase, videoUsecase, thumbnailInteractor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get directories success`() = runTest {
        val response = setOf<UiDirectory>(mockk(relaxed = true))
        coEvery { usecase() } coAnswers {
            delay(100)
            response
        }
        viewModel.initialize(UiMimeType.Photo)
        viewModel.state.test {
            assertEquals(DirectoryViewModel.State.Loading, awaitItem())
            assertEquals(DirectoryViewModel.State.Success(response), awaitItem())
        }
    }

    @Test
    fun `test get directories error`() = runTest {
        val error = RuntimeException()
        coEvery { videoUsecase() } throws error
        viewModel.initialize(UiMimeType.Video)
        viewModel.state.test {
            assertEquals(DirectoryViewModel.State.Error(error), awaitItem())
        }
    }
}
