package eu.peernetwork.ads.data.api

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable

interface AdvertiserApi {
    suspend fun get(id: String): Ads

    suspend fun getAll(filter: Filter, page: Pageable): Page<Ads>

    suspend fun getMetrics(filter: Filter): Metrics

    suspend fun create(id: String)

    suspend fun description(): Description
}
