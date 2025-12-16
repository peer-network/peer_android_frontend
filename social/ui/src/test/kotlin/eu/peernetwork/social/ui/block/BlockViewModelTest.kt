package eu.peernetwork.social.ui.block

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.social.domain.usecase.BlockUsecase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
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
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class BlockViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<BlockUsecase>()

    private lateinit var viewModel: BlockViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = BlockViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test block`() = runTest {
        val userId = "<test-id>"
        val mockData = true
        coEvery { usecase(userId) } coAnswers {
            delay(100)
            mockData
        }
        viewModel.block(userId)
        viewModel.state.test {
            TestCase.assertEquals(BlockViewModel.State.Loading, awaitItem())
            TestCase.assertEquals(BlockViewModel.State.Success(mockData), awaitItem())
        }
    }

    @Test
    fun `test block error`() = runTest {
        val userId = "<test-id>"
        val error = RuntimeException("<test-exception>")

        coEvery { usecase(userId) } throws error

        viewModel.block(userId)
        viewModel.state.test {
            TestCase.assertEquals(BlockViewModel.State.Error(error), awaitItem())
        }
    }
}