package eu.peernetwork.blog.ui.timeline

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.usecase.ViewUsecase
import eu.peernetwork.blog.ui.mock.MockContent
import eu.peernetwork.blog.ui.usecase.FeedUsecase
import eu.peernetwork.core.common.paging.Pageable
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
internal class TimelineViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<FeedUsecase>()

    private val viewUsecase = mockk<ViewUsecase>()

    private lateinit var viewModel: TimelineViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = TimelineViewModel(usecase, viewUsecase)
    }

    @Test
    fun `test get author photos success`() = runTest {
        val mockData = MockContent.post()
        val mockPagingData = PagingData.Companion.from(listOf(mockData))
        val key = listOf(Category.NONE, null).hashCode()
        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem()[key] is TimelineViewModel.State.Loading)
            assertTrue(awaitItem()[key] is TimelineViewModel.State.Success)
        }
    }

    @Test
    fun `test get author photos error`() = runTest {
        val key = listOf(Category.NONE, null).hashCode()
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.load(Pageable(0, 1))
        viewModel.state.test {
            assertEquals(TimelineViewModel.State.Error(error), awaitItem()[key])
        }
    }
}