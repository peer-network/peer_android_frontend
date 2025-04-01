package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.remote.engagement.LikeCommentMutation
import eu.peernetwork.blog.remote.engagement.ReportCommentMutation
import eu.peernetwork.blog.remote.engagement.ResolveActionPostMutation
import eu.peernetwork.blog.remote.mock.EngagementMock
import eu.peernetwork.core.remote.model.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import type.ActionType
import java.util.UUID
import kotlin.test.assertNull

internal class EngagementApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: EngagementApi

    @Before
    fun setup() {
        api = EngagementApiDelegate(client)
    }

    @Test
    fun `test post engagement success`(): Unit = runBlocking {
        val id = "<test-post-id>"
        val engagement = Engagement.Content.Like
        val response = EngagementMock.postResponse()
        val mockData = mockk<ResolveActionPostMutation.Data>()
        val operation = mockk<Operation<ResolveActionPostMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.resolveActionPost } returns response
        coEvery { client.mutation(any<ResolveActionPostMutation>()).execute() } returns mockResponse

        api.post(id, engagement)

        verify { client.mutation(ResolveActionPostMutation(ActionType.LIKE, id)) }
    }

    @Test
    fun `test post engagement error`(): Unit = runBlocking {
        val id = "<test-post-id>"
        val engagement = Engagement.Content.View
        val response = EngagementMock.postResponse().copy(status = Status.ERROR.value)
        val mockData = mockk<ResolveActionPostMutation.Data>()
        val operation = mockk<Operation<ResolveActionPostMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.resolveActionPost } returns response
        coEvery { client.mutation(any<ResolveActionPostMutation>()).execute() } returns mockResponse

        assertNull(try {
            api.post(id, engagement)
        } catch (error: Throwable) {
            null
        })
    }

    @Test
    fun `test like comment`(): Unit = runBlocking {
        val id = "<test-commend-id>"
        val response = EngagementMock.commentResponse()
        val mockData = mockk<LikeCommentMutation.Data>()
        val operation = mockk<Operation<LikeCommentMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.likeComment } returns response
        coEvery { client.mutation(any<LikeCommentMutation>()).execute() } returns mockResponse

        api.comment(id, Engagement.Comment.Like)

        verify { client.mutation(LikeCommentMutation(id)) }
    }

    @Test
    fun `test report comment`(): Unit = runBlocking {
        val id = "<test-commend-id>"
        val response = EngagementMock.reportResponse()
        val mockData = mockk<ReportCommentMutation.Data>()
        val operation = mockk<Operation<ReportCommentMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.reportComment } returns response
        coEvery { client.mutation(any<ReportCommentMutation>()).execute() } returns mockResponse

        api.comment(id, Engagement.Comment.Report)

        verify { client.mutation(ReportCommentMutation(id)) }
    }
}
