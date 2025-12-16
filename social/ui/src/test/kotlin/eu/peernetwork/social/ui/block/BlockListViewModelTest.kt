package eu.peernetwork.social.ui.block

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.mock.BlockMock
import eu.peernetwork.social.ui.usecase.BlockListPagingUsecase
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
internal class BlockListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<BlockListPagingUsecase>()

    private lateinit var viewModel: BlockListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = BlockListViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test block list`() = runTest {
        val id = "<test-id>"
        val mockData = BlockMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.blockList(id, Pageable(0,1))
        viewModel.state.test {
            assertTrue(awaitItem() is BlockListViewModel.State.Loading)
            assertTrue(awaitItem() is BlockListViewModel.State.Success)
        }
    }

    @Test
    fun `test block list error`() = runTest {
        val id = "<test-id>"
        val error = RuntimeException("<test-exception>")

        coEvery { usecase(any()) } returns flow {
            throw error
        }

        viewModel.blockList(id, Pageable(0,1))
        viewModel.state.test {
            assertEquals(BlockListViewModel.State.Error(error), awaitItem())
        }
    }
}