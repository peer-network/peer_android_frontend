package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Point

interface EngagementApi {
    suspend fun points(): List<Point>

    suspend fun post(id: String, engagement: Engagement.Content)

    suspend fun comment(id: String, engagement: Engagement.Comment)
}
