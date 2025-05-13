package eu.peernetwork.social.data.interactor

import app.cash.turbine.test
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import eu.peernetwork.social.domain.usecase.FollowUsecase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConnectionInteractorDelegateTest {
    private val usecase: FollowUsecase = mockk()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var interactor: ConnectionInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        interactor = ConnectionInteractorDelegate(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test connect success`(): Unit = runTest {
        val id = "<test-id>"
        val value = true
        coEvery { usecase(any()) } returns value
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            interactor.connect(id, false)
            assertEquals(mapOf(id to false), awaitItem())
            assertEquals(mapOf(id to value), awaitItem())
        }
    }

    @Test
    fun `test connect error`(): Unit = runTest {
        val id = "<test-id>"
        coEvery { usecase(any()) } throws RuntimeException()
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            try {
                interactor.connect(id, false)
            } catch (_: Throwable) {
                assertEquals(mapOf(id to false), awaitItem())
                assertEquals(emptyMap(), awaitItem())
            }
        }
    }

    @Test
    fun `test clear connection cache`(): Unit = runTest {
        val id = "<test-id>"
        val value = true
        coEvery { usecase(any()) } returns value
        interactor.observe().test {
            assertEquals(emptyMap(), awaitItem())
            interactor.connect(id, false)
            assertEquals(mapOf(id to false), awaitItem())
            assertEquals(mapOf(id to value), awaitItem())
            interactor.clear()
            assertEquals(emptyMap(), awaitItem())
        }
    }
}
