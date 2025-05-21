package eu.peernetwork.media.ui.thumbnail

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.ThumbnailUsecase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ThumbnailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<ThumbnailUsecase>()

    private lateinit var viewModel: ThumbnailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ThumbnailViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialization`() = runTest {
        val bitmap = mockk<Bitmap>()
        val thumbnail = "<test-thumbnail>"
        coEvery { usecase(any()) } coAnswers {
            delay(100)
            bitmap
        }
        viewModel.initialize(thumbnail, UiMimeType.Photo)
        viewModel.state.test {
            assertEquals(ThumbnailViewModel.State.Loading, awaitItem())
            assertEquals(ThumbnailViewModel.State.Success(thumbnail, bitmap), awaitItem())
        }
        viewModel.thumbnails.test {
            assertEquals(mapOf<String, Bitmap>(thumbnail to bitmap), awaitItem())
        }
    }

    @Test
    fun `test initialization error`() = runTest {
        val thumbnail = "<test-thumbnail>"
        val error = RuntimeException()
        coEvery { usecase(any()) } throws error
        viewModel.initialize(thumbnail, UiMimeType.Photo)
        viewModel.state.test {
            assertEquals(ThumbnailViewModel.State.Error(error), awaitItem())
        }
        viewModel.thumbnails.test {
            assertEquals(mapOf<String, Bitmap>(), awaitItem())
        }
    }
}
