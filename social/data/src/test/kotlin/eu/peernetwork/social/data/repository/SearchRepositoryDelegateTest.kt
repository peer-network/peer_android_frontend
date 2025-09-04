package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.model.Tag
import eu.peernetwork.social.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class SearchRepositoryDelegateTest {
    private val api = mockk<SearchApi>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepositoryDelegate(api)
    }

    @Test
    fun `test find post by tag`(): Unit = runBlocking {
        val tag = "<test-tag>"
        val page = Pageable(0, 1)
        val mock = mockk<Tag>()
        coEvery { api.findAllTags(any(), any()) } returns Page(1, 0, listOf(mock))
        val result = repository.findAllTags(tag, page)
        assertEquals(result.items.first(), mock)
        coVerify { api.findAllTags(tag, page) }
    }

    @Test
    fun `test find post by title`(): Unit = runBlocking {
        val title = "<test-title>"
        val page = Pageable(0, 1)
        val mock = mockk<Post>()
        coEvery { api.findPostsByTitle(any(), any()) } returns Page(1, 0, listOf(mock))
        val result = repository.findPostsByTitle(title, page)
        assertEquals(result.items.first(), mock)
        coVerify { api.findPostsByTitle(title, page) }
    }
}
