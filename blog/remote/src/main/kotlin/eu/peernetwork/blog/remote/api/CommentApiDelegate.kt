package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.domain.exception.CommentException
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import eu.peernetwork.blog.remote.mapper.mapToDomain
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.provider.NetworkProvider
import type.CommentType
import javax.inject.Inject
import javax.inject.Named

class CommentApiDelegate @Inject constructor(
    private val provider: NetworkProvider,
    @Named("mediaUrl") private val url: String,
) : CommentApi {
    override suspend fun getAll(id: String, page: Pageable): Page<Comment> {
        val query = GetCommentsQuery(
            postId = Optional.present(id),
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = provider.client().query(query).executeOrThrow()
        val data = response.getOrThrow().listPosts
        val contents = data.affectedRows?.map {
            it.mapToDomain().map {
                it.copy(author = it.author.copy(imageUrl = "$url${it.author.imageUrl}"))
            }
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = page.offset,
            items = contents?.firstOrNull() ?: emptyList()
        )
    }

    override suspend fun comment(postId: String, text: String): Comment {
        val mutation = CreateCommentMutation(
            action = CommentType.COMMENT,
            postId = postId,
            content = text
        )
        val response = provider.client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().createComment
        val content = data.affectedRows?.map { it?.mapToDomain() }
        response.assertOrThrow(data.status, data.ResponseCode)
        return content?.firstOrNull() ?: throw CommentException()
    }
}
