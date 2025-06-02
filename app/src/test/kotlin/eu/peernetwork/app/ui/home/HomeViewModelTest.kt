package eu.peernetwork.app.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import eu.peernetwork.user.domain.usecase.PrincipalUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
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

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val principalUsecase = mockk<PrincipalUsecase>()

    private val retrievableString = mockk<RetrievableString>()

    private val retrievableInteger = mockk<RetrievableInteger>()

    private val publishableInteger = mockk<PublishableInteger>(relaxed = true)

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = HomeViewModel(
            principalUsecase,
            retrievableInteger,
            publishableInteger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initialize state success`() = runTest {
        val page = 3
        val user = "<test-user>"
        every { retrievableString(any()) } returns null
        every { retrievableInteger(any()) } returns page
        coEvery { principalUsecase() } coAnswers {
            delay(100)
            user
        }
        viewModel()
        viewModel.state.test {
            assertEquals(HomeViewModel.State.Loading, awaitItem())
            assertEquals(HomeViewModel.State.Success(user, page), awaitItem())
        }
    }

    @Test
    fun `test initialize state error`() = runTest {
        val error = RuntimeException("<test-error>")
        every { retrievableString(any()) } returns null
        every { retrievableInteger(any()) } returns null
        coEvery { principalUsecase() } throws error
        viewModel()
        viewModel.state.test {
            assertEquals(HomeViewModel.State.Error(error), awaitItem())
        }
    }

    @Test
    fun `test update feed`() = runTest {
        val page = 5
        viewModel.lastVisited(page)
        coVerify { publishableInteger(HomeViewModel.TAG, page) }
    }
}
