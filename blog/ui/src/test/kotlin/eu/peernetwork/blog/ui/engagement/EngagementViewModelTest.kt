package eu.peernetwork.blog.ui.engagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.usecase.ContentUsecase
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiEngagement
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EngagementViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val likeUsecase = mockk<LikeUsecase>()

    private val dislikeUsecase = mockk<DislikeUsecase>()

    private val contentUsecase = mockk<ContentUsecase>()

    private lateinit var viewModel: EngagementViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = EngagementViewModel(likeUsecase, dislikeUsecase, contentUsecase)
    }

    @Test
    fun `test like content success`() = runTest {
        val model = UiEngagement(
            id = "<test-id>",
            likes = 0,
            dislikes = 0,
            isDisliked = false,
            isLiked = false,
            comment = 0
        )
        val mockData = mockk<Content>(relaxed = true)
        every { mockData.id } returns model.id
        every { mockData.likes } returns model.likes + 1
        every { mockData.isLiked } returns true
        coEvery { likeUsecase(any()) } returns Unit
        coEvery { contentUsecase(any()) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.like(model)
        viewModel.state.test {
            assertEquals(EngagementViewModel.State.Loading(mapOf(model.id to mockData.mapToEngagement())), awaitItem())
            assertEquals(EngagementViewModel.State.Success(mapOf(model.id to mockData.mapToEngagement())), awaitItem())
        }
    }

    @Test
    fun `test like content failure`() = runTest {
        val model = UiEngagement(
            id = "<test-id>",
            likes = 0,
            dislikes = 0,
            isDisliked = false,
            isLiked = false,
            comment = 0
        )
        val error = RuntimeException()
        coEvery { likeUsecase(any()) } throws error
        viewModel.like(model)
        viewModel.state.test {
            awaitItem()
            assertEquals(EngagementViewModel.State.Error(model.id, mapOf(model.id to model), error), awaitItem())
        }
    }

    @Test
    fun `test dislike content success`() = runTest {
        val model = UiEngagement(
            id = "<test-id>",
            likes = 0,
            dislikes = 0,
            isDisliked = false,
            isLiked = false,
            comment = 0
        )
        val mockData = mockk<Content>(relaxed = true)
        every { mockData.id } returns model.id
        every { mockData.dislikes } returns model.likes + 1
        every { mockData.isDisliked } returns true
        coEvery { dislikeUsecase(any()) } returns Unit
        coEvery { contentUsecase(any()) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.dislike(model)
        viewModel.state.test {
            assertEquals(EngagementViewModel.State.Loading(mapOf(model.id to mockData.mapToEngagement())), awaitItem())
            assertEquals(EngagementViewModel.State.Success(mapOf(model.id to mockData.mapToEngagement())), awaitItem())
        }
    }

    @Test
    fun `test dislike content failure`() = runTest {
        val model = UiEngagement(
            id = "<test-id>",
            likes = 0,
            dislikes = 0,
            isDisliked = false,
            isLiked = false,
            comment = 0
        )
        val error = RuntimeException()
        coEvery { dislikeUsecase(any()) } throws error
        viewModel.dislike(model)
        viewModel.state.test {
            awaitItem()
            assertEquals(EngagementViewModel.State.Error(model.id, mapOf(model.id to model), error), awaitItem())
        }
    }
}
