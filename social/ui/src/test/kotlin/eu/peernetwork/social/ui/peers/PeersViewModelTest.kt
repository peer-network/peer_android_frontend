package eu.peernetwork.social.ui.peers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.ui.mock.MemberMock
import eu.peernetwork.social.ui.usecase.PeerPagingUsecase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class PeersViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<PeerPagingUsecase>()

    private lateinit var viewModel: PeersViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = PeersViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get peers`() = runTest {
        val mockData = MemberMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.peers(Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is PeersViewModel.State.Loading)
            assertTrue(awaitItem() is PeersViewModel.State.Success)
        }
    }

    @Test
    fun `test get peers error`() = runTest {
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.peers(Pageable(0, 1))
        viewModel.state.test {
            assertEquals(PeersViewModel.State.Error(error), awaitItem())
        }
    }
}
