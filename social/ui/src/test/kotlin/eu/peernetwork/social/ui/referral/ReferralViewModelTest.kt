package eu.peernetwork.social.ui.referral

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.usecase.InviteUsecase
import eu.peernetwork.social.ui.mock.ReferralMock
import eu.peernetwork.social.ui.usecase.ReferralPagingUsecase
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
internal class ReferralViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()
    private val inviteUsecase = mockk<InviteUsecase>()
    private val usecase = mockk<ReferralPagingUsecase>()
    private lateinit var viewModel: ReferralViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ReferralViewModel(usecase, inviteUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test referrals`() = runTest {
        val id = "<test-id>"
        val mockData = ReferralMock.model()
        val mockPagingData = PagingData.from(listOf(mockData))

        coEvery { usecase(any()) } returns flow {
            delay(100)
            emit(mockPagingData)
        }

        viewModel.referral(id, Pageable(0, 1))
        viewModel.state.test {
            assertTrue(awaitItem() is ReferralViewModel.State.Loading)
            assertTrue(awaitItem() is ReferralViewModel.State.Success)
        }
    }

    @Test
    fun `test referrals error`() = runTest {
        val id = "<test-id>"
        val error = RuntimeException("<test-exception>")
        coEvery { usecase(any()) } returns flow {
            throw error
        }
        viewModel.referral(id, Pageable(0, 1))
        viewModel.state.test {
            assertEquals(ReferralViewModel.State.Error(error), awaitItem())
        }
    }
}