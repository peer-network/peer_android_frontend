package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.repository.EngagementRepository

interface BlogProvider {
    fun contentRepository(): ContentRepository

    fun commentRepository(): CommentRepository

    fun engagementRepository(): EngagementRepository
}
