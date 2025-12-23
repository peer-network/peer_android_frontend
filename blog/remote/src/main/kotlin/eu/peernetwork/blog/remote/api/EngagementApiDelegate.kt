package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.core.common.exception.ContentException
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.remote.engagement.LikeCommentMutation
import eu.peernetwork.blog.remote.engagement.PostInteractionsQuery
import eu.peernetwork.blog.remote.engagement.ReportCommentMutation
import eu.peernetwork.blog.remote.engagement.ResolveActionPostMutation
import eu.peernetwork.blog.remote.mapper.mapToAction
import eu.peernetwork.blog.remote.mapper.mapToInteraction
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import javax.inject.Inject
import javax.inject.Named

class EngagementApiDelegate @Inject constructor(
    private val client: RequestClient,
    @Named("mediaUrl") private val url: String,
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

    override suspend fun reactors(
        id: String,
        engagement: Engagement.Content,
        page: Pageable
    ): Page<Author> {
        val query = PostInteractionsQuery(
            GetOnly = engagement.mapToInteraction(),
            postOrCommentId = id,
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().postInteractions ?: throw ContentException()
        response.assertOrThrow(data.status, data.ResponseCode)
        val contents = data.affectedRows?.map {
            Author(
                id = it.id,
                slug = it.slug!!,
                username = it.username!!,
                imageUrl = "$url${it.img}",
                isfollowing = it.isfollowing!!,
                isfollowed = it.isfollowed!!
            )
        }
        return Page(
            count = contents?.size ?: 0,
            offset = page.offset,
            items = contents ?: emptyList()
        )
    }
}
