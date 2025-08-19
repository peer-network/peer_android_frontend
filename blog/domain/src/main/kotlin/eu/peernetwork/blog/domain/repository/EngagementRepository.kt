package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable

interface EngagementRepository {
    suspend fun post(id: String, engagement: Engagement.Content)

    suspend fun comment(id: String, engagement: Engagement.Comment)

    suspend fun reactors(id: String, engagement: Engagement.Content, page: Pageable): Page<Author>
}
