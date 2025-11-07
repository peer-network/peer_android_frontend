package eu.peernetwork.blog.ui.feed.author

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.blog.domain.usecase.ViewUsecase
import eu.peernetwork.blog.ui.mock.MockContent
import eu.peernetwork.blog.ui.usecase.AuthorPostUsecase
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
internal class PhotoViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<AuthorPostUsecase>()

    private val viewUsecase = mockk<ViewUsecase>()

    private lateinit var viewModel: AuthorPostViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = AuthorPostViewModel(usecase, viewUsecase)
    }

    @Test
    fun `test get author photos success`() = runTest {
        val author = "<test-author>"
        val mockData = MockContent.post()
        val mockPagingData = PagingData.Companion.from(listOf(mockData))
        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }
        viewModel.load(author, setOf(), Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is AuthorPostViewModel.State.Loading)
            assertTrue(awaitItem() is AuthorPostViewModel.State.Success)
        }
    }

    @Test
    fun `test get author photos error`() = runTest {
        val author = "<test-author>"
        val error = RuntimeException("<test-exception>")

        coEvery { usecase(any()) } returns flow {
            throw error
        }

        viewModel.load(author, setOf(), Pageable(0, 1))
        viewModel.state.test {
            assertEquals(AuthorPostViewModel.State.Error(error), awaitItem())
        }
    }
}