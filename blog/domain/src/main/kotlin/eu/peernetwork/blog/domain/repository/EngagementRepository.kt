package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Point

interface EngagementRepository {
    suspend fun points(): List<Point>

    suspend fun post(id: String, engagement: Engagement.Content)

    suspend fun comment(id: String, engagement: Engagement.Comment)
}
