package eu.peernetwork.blog.ui.creator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.CreateUsecase
import eu.peernetwork.core.common.usecase.TextEncoderUsecase
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
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
internal class CreatorViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val contentCreationUsecase = mockk<CreateUsecase>(relaxed = true)

    private val mediaEncoderUsecase = mockk<MediaEncoderUsecase>(relaxed = true)

    private val textEncoderUsecase = mockk<TextEncoderUsecase>(relaxed = true)

    private lateinit var viewModel: CreatorViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = CreatorViewModel(
            contentCreationUsecase,
            mediaEncoderUsecase,
            textEncoderUsecase,
        )
    }

    @Test
    fun `test post text content success`() = runTest {
        val text = "<test-text>"
        val description = "#testDescription"
        val draft = UiDraft(
            title = text,
            description = description,
            media = UiMimeType.Text,
            attachment = listOf(),
        )
        val mockData = mockk<UiPost>(relaxed = true)
        every { textEncoderUsecase(any()) } returns text
        coEvery { contentCreationUsecase(any()) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.create(draft)
        viewModel.state.test {
            assertEquals(CreatorViewModel.State.Loading, awaitItem())
            assertEquals(CreatorViewModel.State.Success(mockData), awaitItem())
        }
    }

    @Test
    fun `test post text content error`() = runTest {
        val text = "<test-text>"
        val draft = mockk<UiDraft>(relaxed = true)
        val error = RuntimeException()
        every { draft.media } returns UiMimeType.Text
        every { textEncoderUsecase(any()) } returns text
        coEvery { contentCreationUsecase(any()) } throws error
        viewModel.create(draft)
        viewModel.state.test {
            assertEquals(CreatorViewModel.State.Error(error), awaitItem())
        }
    }
}
