package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Comment

interface EngagementRepository {
    suspend fun like(id: String)

    suspend fun comment(postId: String, text: String): Comment
}
