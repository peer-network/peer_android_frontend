package eu.peernetwork.social.ui.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FeedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val observableInteger = mockk<ObservableInteger>()

    private val publishableInteger = mockk<PublishableInteger>()

    private val dispatcher = UnconfinedTestDispatcher()

    private val mutableState = MutableStateFlow<Int?>(null)

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        every { observableInteger(any()) } returns mutableState
        coEvery { publishableInteger(any(), any()) } answers {
            mutableState.tryEmit(it.invocation.args[1] as Int)
        }

        viewModel = FeedViewModel(observableInteger, publishableInteger)
    }

    @Test
    fun `test feed state`() = runTest {
        val page = 3
        viewModel.updateFeed(page)
        viewModel.state.test {
            assertEquals(FeedViewModel.State.Ready(page), awaitItem())
        }
    }
}
