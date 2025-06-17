package eu.peernetwork.blog.remote.api

import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.remote.engagement.LikeCommentMutation
import eu.peernetwork.blog.remote.engagement.ReportCommentMutation
import eu.peernetwork.blog.remote.engagement.ResolveActionPostMutation
import eu.peernetwork.blog.remote.mapper.mapToAction
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import javax.inject.Inject

class EngagementApiDelegate @Inject constructor(
    private val client: RequestClient,
) : EngagementApi {
    override suspend fun post(id: String, engagement: Engagement.Content) {
        val mutation = ResolveActionPostMutation(engagement.mapToAction(), id)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().resolvePostAction
        response.assertOrThrow(data.status, data.ResponseCode)
    }

    override suspend fun comment(id: String, engagement: Engagement.Comment) {
        when(engagement) {
            Engagement.Comment.Like -> likeComment(id)
            Engagement.Comment.Report -> reportComment(id)
        }
    }

    private suspend fun likeComment(id: String) {
        val mutation = LikeCommentMutation(id)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().likeComment
        response.assertOrThrow(data.status, data.ResponseCode)
    }

    private suspend fun reportComment(id: String) {
        val mutation = ReportCommentMutation(id)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().reportComment
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
