package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class EngagementRepositoryDelegateTest {
    private val api = mockk<EngagementApi>()

    private lateinit var repository: EngagementRepository

    @Before
    fun setup() {
        repository = EngagementRepositoryDelegate(api)
    }

    @Test
    fun `test content engagement`(): Unit = runBlocking {
        val id = "<test-id>"
        val engagement = Engagement.Content.View
        coEvery { api.post(any(), any()) } returns Unit
        repository.post(id, engagement)
        coVerify { api.post(id, engagement) }
    }

    @Test
    fun `test comment engagement`(): Unit = runBlocking {
        val id = "<test-id>"
        val engagement = Engagement.Comment.Like
        coEvery { api.comment(any(), any()) } returns Unit
        repository.comment(id, engagement)
        coVerify { api.comment(id, engagement) }
    }
}
