package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable

interface CommentApi {
    suspend fun getAll(id: String, page: Pageable): Page<Comment>

    suspend fun comment(postId: String, text: String): Comment
}
