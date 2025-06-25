package eu.peernetwork.media.ui.attachment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AttachmentViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val retrievableInteger = mockk<RetrievableInteger>()

    private val publishableInteger = mockk<PublishableInteger>()

    private val observableInteger = mockk<ObservableInteger>()

    private val thumbnailInteractor = mockk<ThumbnailInteractor>()

    private lateinit var viewModel: AttachmentViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { thumbnailInteractor.observe() } returns flowOf(emptyMap())
        viewModel = AttachmentViewModel(
            retrievableInteger,
            publishableInteger,
            observableInteger,
            thumbnailInteractor
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialization`() = runTest {
        val observer = MutableStateFlow<Int?>(null)
        every { observableInteger(any()) } returns observer
        observer.tryEmit(null)
        viewModel.initialize()
        viewModel.state.test {
            assertEquals(AttachmentViewModel.State.Success(0), awaitItem())
        }
    }

    @Test
    fun `test initialization count`() = runTest {
        every { retrievableInteger(any()) } returns 0
        coEvery { publishableInteger(any(), any()) } returns Unit
        viewModel.updatePermissionStatus()
        viewModel.state.test {
            assertEquals(AttachmentViewModel.State.Success(1), awaitItem())
        }
    }
}
