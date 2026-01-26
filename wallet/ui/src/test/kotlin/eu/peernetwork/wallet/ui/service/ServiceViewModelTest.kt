package eu.peernetwork.wallet.ui.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.domain.usecase.TaxUsecase
import eu.peernetwork.wallet.ui.dashboard.DashboardViewModel
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
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class ServiceViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<TaxUsecase>()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = DashboardViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize success`() = runTest {
        val tax = Tax(
            peer = 5.0,
            pool = 3.0,
            burn = 2.0,
            percentage = 10.0
        )
        coEvery { usecase() } coAnswers {
            delay(100)
            tax
        }
        viewModel.initialize()
        viewModel.state.test {
            assertEquals(DashboardViewModel.State.Loading, awaitItem())
            assertEquals(DashboardViewModel.State.Success(tax.mapFromDomain()), awaitItem())
        }
    }

    @Test
    fun `test initialize error`() = runTest {
        val exception = RuntimeException()
        coEvery { usecase() } throws exception
        viewModel.initialize()
        viewModel.state.test {
            assertEquals(DashboardViewModel.State.Error(exception), awaitItem())
        }
    }
}
