package eu.peernetwork.ads.domain.repository

import eu.peernetwork.ads.domain.model.Content

interface ContentRepository {
    suspend fun get(id: String): Content
}
