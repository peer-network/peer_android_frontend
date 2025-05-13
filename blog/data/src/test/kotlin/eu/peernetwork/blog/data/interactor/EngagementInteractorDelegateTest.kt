package eu.peernetwork.blog.data.interactor

import app.cash.turbine.test
import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.repository.EngagementRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EngagementInteractorDelegateTest {
    private val dispatcher = StandardTestDispatcher()

    private val repository = mockk<EngagementRepository>()

    private val pointInteractor = mockk<PointInteractor>()

    private lateinit var interactor: EngagementInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        interactor = EngagementInteractorDelegate(repository, pointInteractor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test like content`(): Unit = runBlocking {
        val id = "<test-id>"
        coEvery { pointInteractor.refresh() } returns listOf()
        coEvery { repository.post(any(), any()) } returns Unit
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            interactor.like(id)
            assertEquals(mapOf(id to EngagementInteractor.Reaction(like = true, dislike = null, commented = null)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test like content error`(): Unit = runBlocking {
        val id = "<test-id>"
        coEvery { pointInteractor.refresh() } returns listOf()
        coEvery { repository.post(any(), any()) } throws RuntimeException()
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            try {
                interactor.like(id)
            } catch (_: Throwable) {}
            assertEquals(mapOf(id to EngagementInteractor.Reaction(like = true, dislike = null, commented = null)), awaitItem())
            assertEquals(mapOf(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test dislike content`(): Unit = runBlocking {
        val id = "<test-id>"
        coEvery { pointInteractor.refresh() } returns listOf()
        coEvery { repository.post(any(), any()) } returns Unit
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            interactor.dislike(id)
            assertEquals(mapOf(id to EngagementInteractor.Reaction(like = null, dislike = true, commented = null)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test dislike content error`(): Unit = runBlocking {
        val id = "<test-id>"
        coEvery { pointInteractor.refresh() } returns listOf()
        coEvery { repository.post(any(), any()) } throws RuntimeException()
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            try {
                interactor.dislike(id)
            } catch (_: Throwable) {}
            assertEquals(mapOf(id to EngagementInteractor.Reaction(like = null, dislike = true, commented = null)), awaitItem())
            assertEquals(mapOf(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test comment content`(): Unit = runBlocking {
        val id = "<test-id>"
        coEvery { pointInteractor.refresh() } returns listOf()
        coEvery { repository.post(any(), any()) } returns Unit
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            interactor.comment(id)
            assertEquals(mapOf(id to EngagementInteractor.Reaction(like = null, dislike = null, commented = 1)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
