package eu.peernetwork.wallet.ui.confirmation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.usecase.ObservableOverviewUsecase
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.domain.usecase.QuoteUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiIntent
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
internal class ConfirmationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val overviewUsecase = mockk<OverviewUsecase>()

    private val quoteUsecase = mockk<QuoteUsecase>()

    private val observableOverviewUsecase = mockk<ObservableOverviewUsecase>()

    private lateinit var viewModel: ConfirmationViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ConfirmationViewModel(overviewUsecase, observableOverviewUsecase, quoteUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize success`() = runTest {
        val balance = BigDecimal(100)
        val quote = Quote(balance)
        val mockData = mockk<Wallet>(relaxed = true)
        every { mockData.balance } returns balance
        coEvery { quoteUsecase(any()) } returns quote
        coEvery { overviewUsecase() } coAnswers {
            delay(100)
            mockData
        }
        viewModel.initialize(UiIntent.Post)
        viewModel.state.test {
            assertEquals(ConfirmationViewModel.State.Loading, awaitItem())
            assertEquals(ConfirmationViewModel.State.Success(quote.mapFromDomain(), mockData.mapFromDomain()), awaitItem())
        }
    }

    @Test
    fun `test initialize error`() = runTest {
        val balance = BigDecimal(100)
        val quote = Quote(balance)
        val error = RuntimeException()
        coEvery { quoteUsecase(any()) } returns quote
        coEvery { overviewUsecase() } throws error
        viewModel.initialize(UiIntent.Post)
        viewModel.state.test {
            assertEquals(ConfirmationViewModel.State.Error(error), awaitItem())
        }
    }
}
