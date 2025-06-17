package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.CommentInteractor
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
internal class CommentInteractorDelegateTest {
    private val dispatcher = StandardTestDispatcher()

    private val repository = mockk<CommentRepository>()

    private val rewardRepository = mockk<RewardRepository>()

    private lateinit var interactor: CommentInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        interactor = CommentInteractorDelegate(repository, rewardRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test post comment success`(): Unit = runBlocking {
        val id = "<test-id>"
        val comment = "<test-comment>"
        coEvery { repository.comment(any(), any()) } returns mockk()
        coEvery { rewardRepository.get() } returns mockk()
        interactor.comment(id, comment)
        coVerify { repository.comment(id, comment) }
        coVerify { rewardRepository.get() }
    }

    @Test
    fun `test post comment error`(): Unit = runBlocking {
        val id = "<test-id>"
        val comment = "<test-comment>"
        coEvery { repository.comment(any(), any()) } throws RuntimeException()
        coEvery { rewardRepository.get() } returns mockk()
        val response = try {
            interactor.comment(id, comment)
        } catch (_: Throwable) {
            null
        }
        assertEquals(response, null)
        coVerify { repository.comment(id, comment) }
        coVerify(exactly = 0) { rewardRepository.get() }
    }
}
