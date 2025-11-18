package eu.peernetwork.ads.data.api

import eu.peernetwork.ads.domain.model.Content

interface ContentApi {
    suspend fun get(id: String): Content
}
