package eu.peernetwork.wallet.ui.transfer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.usecase.TransferUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import io.mockk.coEvery
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
internal class TransferViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<TransferUsecase>()

    private lateinit var viewModel: TransferViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = TransferViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get transfer success`() = runTest {
        val receipt = Receipt("<test-recipient>", BigDecimal(1.0))
        coEvery { usecase(any()) } coAnswers {
            delay(100)
            receipt
        }
        viewModel.transfer(receipt.recipient, receipt.price)
        viewModel.state.test {
            assertEquals(TransferViewModel.State.Loading, awaitItem())
            assertEquals(TransferViewModel.State.Success(receipt.mapFromDomain()), awaitItem())
        }
    }

    @Test
    fun `test get transfer error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase(any()) } throws error
        viewModel.transfer("<test-recipient>", BigDecimal(1.0))
        viewModel.state.test {
            assertEquals(TransferViewModel.State.Error(error), awaitItem())
        }
    }
}
