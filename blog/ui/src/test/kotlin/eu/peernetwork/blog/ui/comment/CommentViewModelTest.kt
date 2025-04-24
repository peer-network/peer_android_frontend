package eu.peernetwork.blog.ui.comment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.peernetwork.blog.domain.usecase.CommentLikeUsecase
import eu.peernetwork.blog.domain.usecase.CommentUsecase
import eu.peernetwork.blog.ui.usecase.CommentsUsecase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CommentViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<CommentUsecase>()

    private val commentsUsecase = mockk<CommentsUsecase>()

    private val commentLikeUsecase = mockk<CommentLikeUsecase>()

    private lateinit var viewModel: CommentViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = CommentViewModel(usecase, commentsUsecase, commentLikeUsecase)
    }

    @Test
    fun `test load comment success`() = runTest {
    }
}
