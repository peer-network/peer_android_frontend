package eu.peernetwork.blog.ui.comment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.AnnotatedString
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.usecase.CommentLikeUsecase
import eu.peernetwork.blog.domain.usecase.CommentUpdateUsecase
import eu.peernetwork.blog.domain.usecase.CommentUsecase
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.usecase.CommentsUsecase
import eu.peernetwork.core.common.paging.Pageable
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CommentViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<CommentUsecase>()

    private val commentsUsecase = mockk<CommentsUsecase>()

    private val updateUsecase = mockk<CommentUpdateUsecase>()

    private val commentLikeUsecase = mockk<CommentLikeUsecase>()

    private lateinit var viewModel: CommentViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        coEvery { updateUsecase(any()) } returns Unit
        viewModel = CommentViewModel(usecase, commentsUsecase, updateUsecase, commentLikeUsecase)
    }

    @Test
    fun `test load comment success`() = runTest {
        val id = "<post-id>"
        val mockData = mockk<UiComment>()
        val pager = PagingData.from(listOf(mockData))
        every { commentsUsecase(any()) } returns flowOf(pager)
        viewModel.load(id, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is CommentViewModel.State.Success)
        }
    }

    @Test
    fun `test post comment success`() = runTest {
        val id = "<post-id>"
        val comment = "<post-comment>"
        val mock = mockk<Comment>(relaxed = true)
        coEvery { mock.id } returns id
        coEvery { usecase(any()) } returns mock
        viewModel.comment(id, comment)
        viewModel.status.test {
            assertEquals(CommentViewModel.Status.Success(CommentViewModel.Intent.Comment, id), awaitItem())
        }
    }

    @Test
    fun `test post comment error`() = runTest {
        val id = "<post-id>"
        val comment = "<post-comment>"
        val error = RuntimeException()
        val mock = mockk<Comment>(relaxed = true)
        coEvery { mock.id } returns id
        coEvery { usecase(any()) } throws error
        viewModel.comment(id, comment)
        viewModel.status.test {
            assertEquals(CommentViewModel.Status.Error(CommentViewModel.Intent.Comment, error), awaitItem())
        }
    }

    @Test
    fun `test like comment success`() = runTest {
        val id = "<post-id>"
        val mock = UiComment(
            id = id,
            author = mockk<UiAuthor>(relaxed = true),
            content = mockk<AnnotatedString>(relaxed = true),
            createdAt = System.currentTimeMillis(),
            likes = 0,
            isLiked = false
        )
        val mockData = mockk<UiComment>()
        val pager = PagingData.from(listOf(mockData))
        val mockContent = flowOf(pager)
        coEvery { commentLikeUsecase(any()) } returns Unit
        every { commentsUsecase(any()) } returns mockContent
        viewModel.load(id, Pageable(0, 1))
        viewModel.like(mock)
        viewModel.status.test {
            val content = awaitItem() as? CommentViewModel.Status.Success<*>?
            assertEquals(content?.content, id)
        }
        viewModel.likes.test {
            val content = awaitItem() as? Map<String, UiComment>?
            assertEquals(content?.get(id), mock.copy(likes = 1, isLiked = true))
        }
    }

    @Test
    fun `test like comment error`() = runTest {
        val id = "<post-id>"
        val mock = mockk<UiComment>(relaxed = true)
        val mockData = mockk<UiComment>()
        val pager = PagingData.from(listOf(mockData))
        val mockContent = flowOf(pager)
        val error = RuntimeException()
        coEvery { mock.id } returns id
        coEvery { commentLikeUsecase(any()) } throws error
        every { commentsUsecase(any()) } returns mockContent
        viewModel.load(id, Pageable(0, 1))
        viewModel.like(mock)
        viewModel.status.test {
            val content = awaitItem() as? CommentViewModel.Status.Error
            assertEquals(content?.error, error)
        }
    }
}
