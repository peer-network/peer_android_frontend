package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.AdPlan
import eu.peernetwork.blog.domain.model.Advert

interface AdvertiserRepository {
    suspend fun create(id: String, ad: AdPlan, refresh: Boolean = false): Advert
}
