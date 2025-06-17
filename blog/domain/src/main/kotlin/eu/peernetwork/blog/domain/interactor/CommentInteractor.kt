package eu.peernetwork.blog.domain.interactor

import eu.peernetwork.blog.domain.model.Comment

interface CommentInteractor {
    suspend fun comment(postId: String, text: String): Comment
}
