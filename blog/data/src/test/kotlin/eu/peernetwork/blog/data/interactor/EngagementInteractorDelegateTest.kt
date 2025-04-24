package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.blog.domain.repository.EngagementRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EngagementInteractorDelegateTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val repository = mockk<EngagementRepository>()

    private lateinit var interactor: EngagementInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        interactor = EngagementInteractorDelegate(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test token refresh`(): Unit = runTest {
        val point = mockk<Point>()

        coEvery { repository.points() } returns listOf(point)

        val emissions = mutableListOf<List<Point>>()
        val result = interactor.refresh()

        interactor.observe().take(1).toList(emissions)

        assertEquals(listOf(point), result)
        assertEquals(listOf(listOf(point)), emissions)
    }
}
