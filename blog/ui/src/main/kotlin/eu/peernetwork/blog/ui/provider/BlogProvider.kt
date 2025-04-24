package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.media.core.provider.MediaProvider

interface BlogProvider : CoreProvider, MediaProvider, InteractorProvider {
    fun contentRepository(): ContentRepository

    fun commentRepository(): CommentRepository

    fun engagementRepository(): EngagementRepository
}
