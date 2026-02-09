package eu.peernetwork.wallet.ui.reward

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.domain.usecase.ObservableRewardUsecase
import eu.peernetwork.wallet.domain.usecase.RewardUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class RewardViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<RewardUsecase>()

    private val observableUsecase = mockk<ObservableRewardUsecase>()

    private lateinit var viewModel: RewardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { observableUsecase() } returns flowOf(emptyList())
        viewModel = RewardViewModel(usecase, observableUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test observe rewards success`() = runTest {
        val rewards = listOf(mockk<Reward>(relaxed = true))
        val uiPoints = rewards.map { it.mapFromDomain() }
        every { observableUsecase() } returns flowOf(rewards)
        viewModel = RewardViewModel(usecase, observableUsecase)
        viewModel.state.test {
            assertEquals(RewardViewModel.State.Success(uiPoints), awaitItem())
        }
    }

    @Test
    fun `test get user reward success`() = runTest {
        coEvery { usecase() } coAnswers {
            delay(100)
            listOf()
        }
        viewModel.getRewards()
        viewModel.state.test {
            TestCase.assertEquals(RewardViewModel.State.Loading, awaitItem())
            TestCase.assertEquals(RewardViewModel.State.Success(listOf()), awaitItem())
        }
    }

    @Test
    fun `test get user point error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase() } throws error
        viewModel.getRewards()
        viewModel.state.test {
            TestCase.assertEquals(RewardViewModel.State.Error(error), awaitItem())
        }
    }
}
