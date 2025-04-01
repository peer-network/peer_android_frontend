package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.ContentType
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.blog.remote.mock.ContentMock
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.model.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import type.ContenType
import type.FilterType
import type.PostenType
import type.SortType
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class ContentApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: ContentApi

    @Before
    fun setup() {
        api = ContentApiDelegate(client)
    }

    @Test
    fun `test filter by post`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val postId = "<test-post-id>"
        val filter = Filter(postId = postId)
        val content = ContentMock.contents()
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.first())
        assertEquals(result.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            postId = Optional.present(postId),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test filter by image`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val filter = Filter(type = setOf(ContentType.IMAGE))
        val content = ContentMock.contents()
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.first())
        assertEquals(result.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            filter = Optional.present(listOf(FilterType.IMAGE)),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test sort by new`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val filter = Filter(criteria = Filter.Criteria.Content())
        val content = ContentMock.contents()
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.first())
        assertEquals(result.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            sort = Optional.present(SortType.NEWEST),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test filter error state`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val filter = Filter(type = setOf(ContentType.IMAGE))
        val content = ContentMock.contents().copy(status = Status.ERROR.value)
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = try {
            api.get(filter, page)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test create text content type`(): Unit = runBlocking {
        val content = ContentMock.content()
        val mockData = mockk<CreatePostMutation.Data>()
        val operation = mockk<Operation<CreatePostMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.createPost } returns content
        coEvery { client.mutation(any<CreatePostMutation>()).execute() } returns mockResponse

        val draft = Draft(
            title = "<test-title>",
            description = "<test-description>",
            tags = listOf("<test-tag>"),
            type = ContentType.TEXT,
        )
        val result = api.create(draft, listOf())

        assertEquals(result.id, content.affectedRows?.id)

        verify { client.mutation(CreatePostMutation(
            action = PostenType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = ContenType.text,
            tags = Optional.present(draft.tags)
        )) }
    }

    @Test
    fun `test create content error`(): Unit = runBlocking {
        val content = ContentMock.content().copy(status = Status.ERROR.value)
        val mockData = mockk<CreatePostMutation.Data>()
        val operation = mockk<Operation<CreatePostMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.createPost } returns content
        coEvery { client.mutation(any<CreatePostMutation>()).execute() } returns mockResponse

        val draft = Draft(
            title = "<test-title>",
            description = "<test-description>",
            tags = listOf("<test-tag>"),
            type = ContentType.TEXT,
        )
        val result = try {
            api.create(draft, listOf())
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test create media content type`(): Unit = runBlocking {
        val content = ContentMock.content()
        val mockData = mockk<CreatePostMutation.Data>()
        val operation = mockk<Operation<CreatePostMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.createPost } returns content
        coEvery { client.mutation(any<CreatePostMutation>()).execute() } returns mockResponse

        val media = "<test-media>"
        val cover = "<test-cover>"
        val draft = Draft(
            title = "<test-title>",
            description = "<test-description>",
            tags = listOf("<test-tag>"),
            type = ContentType.AUDIO,
        )
        val result = api.create(draft, listOf(media), cover)

        assertEquals(result.id, content.affectedRows?.id)

        verify { client.mutation(CreatePostMutation(
            action = PostenType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = ContenType.audio,
            media = Optional.present(listOf(media)),
            cover = Optional.present(listOf(cover)),
            tags = Optional.present(draft.tags)
        )) }
    }
}
