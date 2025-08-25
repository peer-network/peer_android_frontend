package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.blog.remote.mock.ContentMock
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.core.remote.api.RequestClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import type.ContentFilterType
import type.ContentType
import type.PostFilterType
import type.PostSortType
import type.PostType
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class ContentApiDelegateTest {
    private val gson = Gson()

    private val client = mockk<ApolloClient>()

    private val sessionInteractor = mockk<SessionInteractor>(relaxed = true)

    private val url = "http://locahost"

    private lateinit var api: ContentApi

    @Before
    fun setup() {
        coEvery { sessionInteractor.mode() } returns ContentFilterType.MYGRANDMALIKES.name
        api = ContentApiDelegate(gson, url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        }, sessionInteractor)
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

        every { mockData.listPosts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            postId = Optional.present(postId),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset),
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
        )) }
    }

    @Test
    fun `test filter by image`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val filter = Filter(type = setOf(Content.Type.IMAGE))
        val content = ContentMock.contents()
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listPosts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            filter = Optional.present(listOf(PostFilterType.IMAGE)),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset),
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
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

        every { mockData.listPosts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.get(filter, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.first()?.id)

        verify { client.query(GetallpostsQuery(
            sort = Optional.present(PostSortType.NEWEST),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset),
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
        )) }
    }

    @Test
    fun `test filter error state`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val filter = Filter(type = setOf(Content.Type.IMAGE))
        val content = ContentMock.contents().copy(status = Status.ERROR.value)
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listPosts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = try {
            api.get(filter, page)
        } catch (_: Throwable) {
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

        val type = Draft.Type.Text(listOf("<test-text>"))
        val draft = Draft(
            title = "<test-title>",
            description = "<test-description>",
            tags = listOf("<test-tag>"),
            type = type,
        )
        val result = api.create(draft)

        assertEquals(result.id, content.affectedRows?.id)

        verify { client.mutation(CreatePostMutation(
            action = PostType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = ContentType.text,
            media = Optional.present(type.files),
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
            type = Draft.Type.Text(listOf("<test-text>")),
        )
        val result = try {
            api.create(draft)
        } catch (_: Throwable) {
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
            type = Draft.Type.Audio(listOf(media), cover),
        )
        val result = api.create(draft)

        assertEquals(result.id, content.affectedRows?.id)

        verify { client.mutation(CreatePostMutation(
            action = PostType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = ContentType.audio,
            media = Optional.present(listOf(media)),
            cover = Optional.present(listOf(cover)),
            tags = Optional.present(draft.tags)
        )) }
    }
}
