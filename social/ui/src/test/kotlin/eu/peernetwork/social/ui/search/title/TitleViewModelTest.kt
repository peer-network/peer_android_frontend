package eu.peernetwork.social.ui.search.title

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.ui.mock.PostMock
import eu.peernetwork.social.ui.usecase.TitleUsecase
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
internal class TitleViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<TitleUsecase>()

    private lateinit var viewModel: TitleViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = TitleViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test search title success`() = runTest {
        val title = "<test-title>"
        val mockData = PostMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.search(title, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is TitleViewModel.State.Loading)
            assertTrue(awaitItem() is TitleViewModel.State.Success)
        }
    }

    @Test
    fun `test search title error`() = runTest {
        val title = "<test-title>"
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.search(title, Pageable(0, 1))
        viewModel.state.test {
            assertEquals(TitleViewModel.State.Error(error), awaitItem())
        }
    }
}
