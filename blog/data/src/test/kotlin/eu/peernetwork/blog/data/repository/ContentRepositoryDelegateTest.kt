package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.ContentType
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
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
        coEvery { api.get(any(), any()) } returns listOf(mock)
        val result = repository.get(id)
        assertEquals(result, mock)
        coVerify { api.get(Filter(postId = id), Pageable(0, 1)) }
    }

    @Test
    fun `test get all contents`(): Unit = runBlocking {
        val id = "<test-id>"
        val page = Pageable(0, 1)
        val filter = Filter(postId = id)
        val mock = mockk<Content>()
        coEvery { api.get(any(), any()) } returns listOf(mock)
        val result = repository.getAll(filter, page)
        assertEquals(result, listOf(mock))
        coVerify { api.get(Filter(postId = id), page) }
    }

    @Test
    fun `test create text contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        coEvery { api.create(any(), any(), any()) } returns mock
        val result = repository.create(draft, ContentType.Text)
        assertEquals(result, mock)
        coVerify { api.create(draft, listOf()) }
    }

    @Test
    fun `test create video contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val video = "http://localhost/test-video-url"
        coEvery { api.create(any(), any(), any()) } returns mock
        val result = repository.create(draft, ContentType.Video(listOf(video)))
        assertEquals(result, mock)
        coVerify { api.create(draft, listOf(video)) }
    }

    @Test
    fun `test create image contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val image = "http://localhost/test-image-url"
        coEvery { api.create(any(), any(), any()) } returns mock
        val result = repository.create(draft, ContentType.Image(listOf(image)))
        assertEquals(result, mock)
        coVerify { api.create(draft, listOf(image)) }
    }

    @Test
    fun `test create audio contents`(): Unit = runBlocking {
        val mock = mockk<Content>()
        val draft = mockk<Draft>()
        val audio = "http://localhost/test-audio-url"
        val cover = "http://localhost/test-audio-cover"
        coEvery { api.create(any(), any(), any()) } returns mock
        val result = repository.create(draft, ContentType.Audio(listOf(audio), cover))
        assertEquals(result, mock)
        coVerify { api.create(draft, listOf(audio), cover) }
    }
}
