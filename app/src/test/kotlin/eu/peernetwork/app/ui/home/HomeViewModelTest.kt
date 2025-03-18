package eu.peernetwork.app.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import io.mockk.coEvery
import io.mockk.coVerify
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
internal class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val retrievableInteger = mockk<RetrievableInteger>()

    private val publishableInteger = mockk<PublishableInteger>()

    private val dispatcher = UnconfinedTestDispatcher()

    private val mutableState = MutableStateFlow<Int?>(null)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        every { retrievableInteger(any()) } answers {
            mutableState.value
        }
        coEvery { publishableInteger(any(), any()) } answers {
            mutableState.tryEmit(it.invocation.args[1] as Int)
        }
        viewModel = HomeViewModel(retrievableInteger, publishableInteger)
    }

    @Test
    fun `test feed state`() = runTest {
        val page = 3
        mutableState.tryEmit(page)
        val viewModel = HomeViewModel(retrievableInteger, publishableInteger)
        viewModel.state.test {
            assertEquals(HomeViewModel.State.Ready(page), awaitItem())
        }
    }

    @Test
    fun `test update feed`() = runTest {
        val page = 3
        viewModel.updateFeed(page)
        coVerify { publishableInteger(any(), page) }
    }
}
