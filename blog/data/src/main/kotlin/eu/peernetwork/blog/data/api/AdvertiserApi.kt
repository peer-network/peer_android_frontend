package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.AdPlan
import eu.peernetwork.blog.domain.model.Advert

interface AdvertiserApi {
    suspend fun create(id: String, ad: AdPlan, refresh: Boolean = false): Advert
}
