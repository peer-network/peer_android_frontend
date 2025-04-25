package eu.peernetwork.blog.ui.point

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.blog.domain.usecase.ObservePointUsecase
import eu.peernetwork.blog.domain.usecase.PointUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
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
internal class PointViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val pointUsecase = mockk<PointUsecase>()

    private val observePointUsecase = mockk<ObservePointUsecase>()

    private lateinit var viewModel: PointViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { observePointUsecase() } returns flowOf(emptyList())
        viewModel = PointViewModel(pointUsecase, observePointUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test observe points success`() = runTest {
        val points = listOf(mockk<Point>(relaxed = true))
        val uiPoints = points.map { it.mapFromDomain() }
        every { observePointUsecase() } returns flowOf(points)
        viewModel = PointViewModel(pointUsecase, observePointUsecase)
        viewModel.state.test {
            assertEquals(PointViewModel.State.Success(uiPoints), awaitItem())
        }
    }

    @Test
    fun `test get user point success`() = runTest {
        coEvery { pointUsecase() } coAnswers {
            delay(100)
            listOf()
        }
        viewModel.getPoints()
        viewModel.state.test {
            TestCase.assertEquals(PointViewModel.State.Loading, awaitItem())
            TestCase.assertEquals(PointViewModel.State.Success(listOf()), awaitItem())
        }
    }

    @Test
    fun `test get user point error`() = runTest {
        val error = RuntimeException()
        coEvery { pointUsecase() } coAnswers {
            delay(100)
            throw error
        }
        viewModel.getPoints()
        viewModel.state.test {
            TestCase.assertEquals(PointViewModel.State.Loading, awaitItem())
            TestCase.assertEquals(PointViewModel.State.Error(error), awaitItem())
        }
    }
}
