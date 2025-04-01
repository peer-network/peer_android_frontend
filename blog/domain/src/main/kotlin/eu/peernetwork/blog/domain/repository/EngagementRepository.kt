package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Engagement

interface EngagementRepository {
    suspend fun post(id: String, engagement: Engagement.Content)

    suspend fun comment(id: String, engagement: Engagement.Comment)
}
