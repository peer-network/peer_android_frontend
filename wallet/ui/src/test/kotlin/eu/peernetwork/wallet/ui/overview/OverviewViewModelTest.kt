package eu.peernetwork.wallet.ui.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.usecase.ObservableOverviewUsecase
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
import java.math.BigDecimal
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class OverviewViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<OverviewUsecase>()

    private val observableUsecase = mockk<ObservableOverviewUsecase>()

    private lateinit var viewModel: OverviewViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = OverviewViewModel(usecase, observableUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get overview success`() = runTest {
        val balance = BigDecimal(100)
        val mockData = mockk<Wallet>(relaxed = true)
        every { mockData.balance } returns balance
        coEvery { usecase() } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getBalance()
        viewModel.state.test {
            assertEquals(OverviewViewModel.State.Loading, awaitItem())
            assertEquals(OverviewViewModel.State.Success(mockData.mapFromDomain()), awaitItem())
        }
    }

    @Test
    fun `test get overview error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase() } throws error
        viewModel.getBalance()
        viewModel.state.test {
            assertEquals(OverviewViewModel.State.Error(error), awaitItem())
        }
    }
}
