package eu.peernetwork.app.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import eu.peernetwork.user.domain.usecase.TokenUsecase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val tokenUsecase = mockk<TokenUsecase>()

    private val tokenObserverUsecase = mockk<TokenObserverUsecase>()

    private val tokenObserver = MutableSharedFlow<Token?>(replay = 1)

    private lateinit var viewModel: MainViewModel

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { tokenUsecase() } returns null
        every { tokenObserverUsecase() } returns tokenObserver
        viewModel = MainViewModel(tokenUsecase, tokenObserverUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test setup state`() = runTest {
        tokenObserver.tryEmit(null)
        viewModel.state.test {
            assertEquals(MainViewModel.State(null), awaitItem())
        }
    }

    @Test
    fun `test authenticated state`() = runTest {
        val token = mockk<Token>()
        tokenObserver.tryEmit(token)
        viewModel.state.test {
            assertEquals(MainViewModel.State(token), awaitItem())
        }
    }
}
