package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.domain.repository.ContentMultipartRepository
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.blog.domain.repository.MultipartRepository

interface RepositoryProvider {
    fun contentRepository(): ContentRepository

    fun commentRepository(): CommentRepository

    fun engagementRepository(): EngagementRepository

    fun contentMultipartRepository(): ContentMultipartRepository
}
