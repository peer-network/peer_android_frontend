package eu.peernetwork.social.ui.followings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.ui.mock.MemberMock
import eu.peernetwork.social.ui.usecase.FollowingPagingUsecase
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
internal class FollowingsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<FollowingPagingUsecase>()

    private lateinit var viewModel: FollowingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = FollowingsViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test follow member`() = runTest {
        val id = "<test-id>"
        val mockData = MemberMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.followers(id, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is FollowingsViewModel.State.Loading)
            assertTrue(awaitItem() is FollowingsViewModel.State.Success)
        }
    }

    @Test
    fun `test follow member error`() = runTest {
        val id = "<test-id>"
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.followers(id, Pageable(0, 1))
        viewModel.state.test {
            assertEquals(FollowingsViewModel.State.Error(error), awaitItem())
        }
    }
}
