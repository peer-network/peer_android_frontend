package eu.peernetwork.app.ui.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class FeedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val retrievableInteger = mockk<RetrievableInteger>()

    private val publishableInteger = mockk<PublishableInteger>()

    private val observableInteger = mockk<ObservableInteger>()

    private val dispatcher = UnconfinedTestDispatcher()

    private val mutableState = MutableStateFlow<Int?>(null)

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { observableInteger(any()) } returns mutableState
        every { retrievableInteger(any()) } answers {
            mutableState.value
        }
        coEvery { publishableInteger(any(), any()) } answers {
            mutableState.tryEmit(it.invocation.args[1] as Int)
        }
        viewModel = FeedViewModel(retrievableInteger, observableInteger, publishableInteger)
    }

    @Test
    fun `test initialize state`() = runTest {
        val page = 3
        mutableState.tryEmit(page)
        val viewModel = FeedViewModel(retrievableInteger, observableInteger, publishableInteger)
        viewModel.state.test {
            assertEquals(FeedViewModel.State.Initialize(page), awaitItem())
        }
    }

    @Test
    fun `test update feed`() = runTest {
        val page = 5
        viewModel.lastVisited(page)
        viewModel.state.test {
            assertEquals(FeedViewModel.State.Initialize(page), awaitItem())
        }
    }
}