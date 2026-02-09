package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import eu.peernetwork.blog.remote.mapper.mapToDomain
import eu.peernetwork.blog.remote.mapper.mapToMode
import eu.peernetwork.core.common.exception.ContentException
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import type.CommentType
import javax.inject.Inject
import javax.inject.Named

class CommentApiDelegate @Inject constructor(
    private val client: RequestClient,
    @Named("mediaUrl") private val url: String,
    private val sessionInteractor: SessionInteractor
) : CommentApi {
    override suspend fun getAll(id: String, page: Pageable): Page<Comment> {
        val query = GetCommentsQuery(
            postId = Optional.present(id),
            contentFilterBy = Optional.present(sessionInteractor.mode().mapToMode()),
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listPosts
        val contents = data.affectedRows?.map { comments ->
            comments.mapToDomain().map {
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
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().createComment
        val content = data.affectedRows?.map { it?.mapToDomain() }
        response.assertOrThrow(data.status, data.ResponseCode)
        return content?.firstOrNull() ?: throw ContentException()
    }
}
