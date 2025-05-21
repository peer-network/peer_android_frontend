package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.remote.mock.PostMock
import eu.peernetwork.social.remote.mock.TagMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import social.social.eu.peernetwork.social.remote.GetallpostsQuery
import social.social.eu.peernetwork.social.remote.SearchTagsQuery
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class SearchApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private val url = "http://locahost"

    private lateinit var api: SearchApi

    @Before
    fun setup() {
        api = SearchApiDelegate(url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test find post by tag`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val tag = "<test-tag>"
        val content = TagMock.tags()
        val mockData = mockk<SearchTagsQuery.Data>()
        val operation = mockk<Operation<SearchTagsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.searchTags } returns content
        coEvery { client.query(any<SearchTagsQuery>()).execute() } returns mockResponse

        val result = api.findAllTags(tag, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().value, content.affectedRows?.first()?.name)

        verify { client.query(SearchTagsQuery(
            tagName = tag,
            limit = page.limit,
            offset = page.offset
        )) }
    }

    @Test
    fun `test find post by tag error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val tag = "<test-tag>"
        val content = TagMock.tags().copy(status = Status.ERROR.value)
        val mockData = mockk<SearchTagsQuery.Data>()
        val operation = mockk<Operation<SearchTagsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.searchTags } returns content
        coEvery { client.query(any<SearchTagsQuery>()).execute() } returns mockResponse

        val result = try {
            api.findAllTags(tag, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test find post by title`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val title = "<test-post-title>"
        val content = PostMock.posts()
        val mockData = mockk<GetallpostsQuery.Data>()
        val operation = mockk<Operation<GetallpostsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listPosts } returns content
        coEvery { client.query(any<GetallpostsQuery>()).execute() } returns mockResponse

        val result = api.findPostsByTitle(title, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().title, content.affectedRows?.first()?.title)

        verify { client.query(GetallpostsQuery(
            title = Optional.present(title),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test find post by title error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val title = "<test-post-title>"
        val content = PostMock.posts().copy(status = Status.ERROR.value)
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
            api.findPostsByTitle(title, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }
}
