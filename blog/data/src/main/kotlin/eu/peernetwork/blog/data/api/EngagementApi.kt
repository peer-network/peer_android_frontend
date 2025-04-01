package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Engagement

interface EngagementApi {
    suspend fun post(id: String, engagement: Engagement.Content)

    suspend fun comment(id: String, engagement: Engagement.Comment)
}
