package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentRepository
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
internal class ContentInteractorDelegateTest {
    private val dispatcher = StandardTestDispatcher()

    private val repository = mockk<ContentRepository>()

    private val rewardRepository = mockk<RewardRepository>()

    private lateinit var interactor: ContentInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        interactor = ContentInteractorDelegate(repository, rewardRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test post content success`(): Unit = runBlocking {
        val draft = mockk<Draft>()
        coEvery { repository.create(any()) } returns mockk()
        coEvery { rewardRepository.get() } returns mockk()
        interactor.create(draft)
        coVerify { repository.create(draft) }
        coVerify { rewardRepository.get() }
    }

    @Test
    fun `test post content error`(): Unit = runBlocking {
        val draft = mockk<Draft>()
        coEvery { repository.create(any()) } throws RuntimeException()
        coEvery { rewardRepository.get() } returns mockk()
        val response = try {
            interactor.create(draft)
        } catch (_: Throwable) {
            null
        }
        assertEquals(response, null)
        coVerify { repository.create(draft) }
        coVerify(exactly = 0) { rewardRepository.get() }
    }
}
