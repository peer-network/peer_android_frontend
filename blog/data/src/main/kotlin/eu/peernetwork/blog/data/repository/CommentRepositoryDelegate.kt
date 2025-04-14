package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import javax.inject.Inject

class CommentRepositoryDelegate @Inject constructor(
    private val api: CommentApi
) : CommentRepository {
    override suspend fun getAll(id: String, page: Pageable): Page<Comment> {
        return api.getAll(id, page)
    }

    override suspend fun comment(postId: String, text: String): Comment {
        return api.comment(postId, text)
    }
}
