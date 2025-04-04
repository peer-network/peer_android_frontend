package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import eu.peernetwork.blog.remote.mock.CommentMock
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.model.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import type.CommentsType
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class CommentApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: CommentApi

    @Before
    fun setup() {
        api = CommentApiDelegate(client)
    }

    @Test
    fun `test get all comments success`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-post-id>"
        val content = CommentMock.comments()
        val mockData = mockk<GetCommentsQuery.Data>()
        val operation = mockk<Operation<GetCommentsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetCommentsQuery>()).execute() } returns mockResponse

        val result = api.getAll(id, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.first()?.comments?.first()?.commentid)

        verify { client.query(GetCommentsQuery(
            postId = Optional.present(id),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test get all comments error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-post-id>"
        val content = CommentMock.comments().copy(status = Status.ERROR.value)
        val mockData = mockk<GetCommentsQuery.Data>()
        val operation = mockk<Operation<GetCommentsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getallposts } returns content
        coEvery { client.query(any<GetCommentsQuery>()).execute() } returns mockResponse

        val result = try {
            api.getAll(id, page)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        verify { client.query(GetCommentsQuery(
            postId = Optional.present(id),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test create comments success`(): Unit = runBlocking {
        val id = "<test-post-id>"
        val comment = "<test-comment>"
        val content = CommentMock.comment()
        val mockData = mockk<CreateCommentMutation.Data>()
        val operation = mockk<Operation<CreateCommentMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.createComment } returns content
        coEvery { client.mutation(any<CreateCommentMutation>()).execute() } returns mockResponse

        val result = api.comment(id, comment)

        assertEquals(result.id, content.affectedRows?.first()?.commentid)

        verify { client.mutation(CreateCommentMutation(
            action = CommentsType.COMMENT,
            postId = id,
            content = comment
        )) }
    }

    @Test
    fun `test create comments error`(): Unit = runBlocking {
        val id = "<test-post-id>"
        val comment = "<test-comment>"
        val content = CommentMock.comment().copy(status = Status.ERROR.value)
        val mockData = mockk<CreateCommentMutation.Data>()
        val operation = mockk<Operation<CreateCommentMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.createComment } returns content
        coEvery { client.mutation(any<CreateCommentMutation>()).execute() } returns mockResponse

        val result = try {
            api.comment(id, comment)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        verify { client.mutation(CreateCommentMutation(
            action = CommentsType.COMMENT,
            postId = id,
            content = comment
        )) }
    }
}
