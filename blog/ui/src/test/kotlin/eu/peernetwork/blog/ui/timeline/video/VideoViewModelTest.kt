package eu.peernetwork.blog.ui.timeline.video

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.blog.ui.mock.MockContent
import eu.peernetwork.blog.ui.usecase.UserVideosUsecase
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class VideoViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<UserVideosUsecase>()

    private val interactor = mockk<ThumbnailInteractor>(relaxed = true)

    private lateinit var viewModel: VideoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = VideoViewModel(usecase, interactor)
    }

    @Test
    fun `test get author photos success`() = runTest {
        val mockData = MockContent.video()
        val mockPagingData = PagingData.from(listOf(mockData))
        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is VideoViewModel.State.Loading)
            assertTrue(awaitItem() is VideoViewModel.State.Success)
        }
    }

    @Test
    fun `test get author videos error`() = runTest {
        val error = RuntimeException("<test-exception>")

        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertEquals(VideoViewModel.State.Error(error), awaitItem())
        }
    }
}
