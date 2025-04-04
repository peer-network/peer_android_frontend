package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class CommentRepositoryDelegateTest {
    private val api = mockk<CommentApi>()

    private lateinit var repository: CommentRepository

    @Before
    fun setup() {
        repository = CommentRepositoryDelegate(api)
    }

    @Test
    fun `test get all comment`(): Unit = runBlocking {
        val id = "<test-id>"
        val page = Pageable(0, 1)
        val mock = mockk<Comment>()

        coEvery { api.getAll(any(), any()) } returns Page(1, 0, listOf(mock))

        val result = repository.getAll(id, page)

        assertEquals(result.items.first(), mock)
        coVerify { api.getAll(id, page) }
    }

    @Test
    fun `test post comment`(): Unit = runBlocking {
        val id = "<test-id>"
        val comment = "<test-comment>"
        val mock = mockk<Comment>()

        coEvery { api.comment(any(), any()) } returns mock

        val result = repository.comment(id, comment)

        assertEquals(result, mock)
        coVerify { api.comment(id, comment) }
    }
}
