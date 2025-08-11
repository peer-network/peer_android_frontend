package eu.peernetwork.blog.ui.timeline.photo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.blog.ui.mock.MockContent
import eu.peernetwork.blog.ui.usecase.UserPostsUsecase
import eu.peernetwork.core.common.model.Pageable
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class PhotoViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<UserPostsUsecase>()

    private lateinit var viewModel: PhotoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = PhotoViewModel(usecase)
    }

    @Test
    fun `test get author photos success`() = runTest {
        val mockData = MockContent.post()
        val mockPagingData = PagingData.from(listOf(mockData))
        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is PhotoViewModel.State.Loading)
            assertTrue(awaitItem() is PhotoViewModel.State.Success)
        }
    }

    @Test
    fun `test get author photos error`() = runTest {
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertEquals(PhotoViewModel.State.Error(error), awaitItem())
        }
    }
}
