package eu.peernetwork.app.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import eu.peernetwork.persistence.domain.retrievable.RetrievableBoolean
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

    private val tokenObserverUsecase = mockk<TokenObserverUsecase>()

    private val retrievableBoolean = mockk<RetrievableBoolean>()

    private val publishable = mockk<PublishableBoolean>()

    private val tokenObserver = MutableSharedFlow<Token?>(replay = 1)

    private val sessionObserver = MutableSharedFlow<Boolean?>(replay = 1)

    private lateinit var viewModel: MainViewModel

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        sessionObserver.tryEmit(null)

        every { tokenObserverUsecase() } returns tokenObserver

        viewModel = MainViewModel(tokenObserverUsecase, retrievableBoolean, publishable)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loading state`() = runTest {
        every { tokenObserverUsecase() } coAnswers {
            delay(100)
            tokenObserver
        }
        viewModel.state.test {
            assertEquals(MainViewModel.State.Loading, awaitItem())
        }
    }

    @Test
    fun `test setup state`() = runTest {
        tokenObserver.tryEmit(null)
        every { retrievableBoolean(any()) } returns false
        viewModel.state.test {
            assertEquals(MainViewModel.State.Startup(false), awaitItem())
        }
    }

    @Test
    fun `test setup with registration`() = runTest {
        tokenObserver.tryEmit(null)
        every { retrievableBoolean(any()) } returns true
        viewModel.state.test {
            assertEquals(MainViewModel.State.Startup(true), awaitItem())
        }
    }

    @Test
    fun `test authenticated state`() = runTest {
        val token = mockk<Token>()
        tokenObserver.tryEmit(token)
        viewModel.state.test {
            assertEquals(MainViewModel.State.Authenticated(token), awaitItem())
        }
    }
}
