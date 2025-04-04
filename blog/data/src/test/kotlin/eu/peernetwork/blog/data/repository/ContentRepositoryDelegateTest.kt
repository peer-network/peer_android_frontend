package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ContentRepositoryDelegateTest {
    private val api = mockk<ContentApi>()

    private lateinit var repository: ContentRepository

    @Before
    fun setup() {
        repository = ContentRepositoryDelegate(api)
    }

    @Test
    fun `test get content by id`(): Unit = runBlocking {
        val id = "<test-id>"
        val mock = mockk<Content>()
        coEvery { api.get(any(), any()) } returns Page(1, 0, listOf(mock))
        val result = repository.get(id)
        assertEquals(result, mock)
        coVerify { api.get(Filter(postId = id), Pageable(0, 1)) }
    }

    @Test
    fun `test get content by author`(): Unit = runBlocking {
        val author = "<test-author>"
        val page = Pageable(0, 1)
        val mock = mockk<Content>()
        coEvery { api.get(any(), any()) } returns Page(1, 0, listOf(mock))
        val result = repository.getAll(Filter(author = author), page)
        assertEquals(result.items.first(), mock)
        coVerify { api.get(Filter(author = author), page) }
    }

    @Test
    fun `test get all contents`(): Unit = runBlocking {
        val id = "<test-id>"
        val page = Pageable(0, 1)
        val filter = Filter(postId = id)
        val mock = mockk<Content>()
        coEvery { api.get(any(), any()) } returns Page(1, 0, listOf(mock))
        val result = repository.getAll(filter, page)
        assertEquals(result.items, listOf(mock))
        coVerify { api.get(Filter(postId = id), page) }
    }

    @Test
    fun `test create text contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        coEvery { draft.type } returns Draft.Type.Text
        coEvery { api.create(any()) } returns mock
        val result = repository.create(draft)
        assertEquals(result, mock)
        coVerify { api.create(draft) }
    }

    @Test
    fun `test create video contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val video = "http://localhost/test-video-url"
        coEvery { draft.type } returns Draft.Type.Video(listOf(video))
        coEvery { api.create(any()) } returns mock
        val result = repository.create(draft)
        assertEquals(result, mock)
        coVerify { api.create(draft) }
    }

    @Test
    fun `test create image contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val image = "http://localhost/test-image-url"
        coEvery { draft.type } returns Draft.Type.Image(listOf(image))
        coEvery { api.create(any()) } returns mock
        val result = repository.create(draft)
        assertEquals(result, mock)
        coVerify { api.create(draft) }
    }

    @Test
    fun `test create audio contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val audio = "http://localhost/test-audio-url"
        val cover = "http://localhost/test-audio-cover"
        coEvery { draft.type } returns Draft.Type.Audio(listOf(audio), cover)
        coEvery { api.create(any()) } returns mock
        val result = repository.create(draft)
        assertEquals(result, mock)
        coVerify { api.create(draft) }
    }
}
