package eu.peernetwork.social.ui.search.member

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.mock.MemberMock
import eu.peernetwork.social.ui.usecase.MemberUsecase
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
internal class MemberViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<MemberUsecase>()

    private lateinit var viewModel: MemberViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = MemberViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test search member success`() = runTest {
        val username = "<test-username>"
        val mockData = MemberMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.search(username, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is MemberViewModel.State.Loading)
            assertTrue(awaitItem() is MemberViewModel.State.Success)
        }
    }

    @Test
    fun `test search member error`() = runTest {
        val username = "<test-username>"
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.search(username, Pageable(0, 1))
        viewModel.state.test {
            assertEquals(MemberViewModel.State.Error(error), awaitItem())
        }
    }
}
