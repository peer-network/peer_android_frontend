package eu.peernetwork.social.ui.search.tag

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.mock.TagMock
import eu.peernetwork.social.ui.usecase.TagUsecase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class TagViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<TagUsecase>()

    private lateinit var viewModel: TagViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = TagViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test search tag success`() = runTest {
        val tag = "<test-tag>"
        val mockData = TagMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.search(tag, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is TagViewModel.State.Loading)
            assertTrue(awaitItem() is TagViewModel.State.Success)
        }
    }

    @Test
    fun `test search tag error`() = runTest {
        val tag = "<test-tag>"
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.search(tag, Pageable(0, 1))
        viewModel.state.test {
            assertEquals(TagViewModel.State.Error(error), awaitItem())
        }
    }
}
