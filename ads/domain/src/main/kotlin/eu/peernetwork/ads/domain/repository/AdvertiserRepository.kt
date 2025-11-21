package eu.peernetwork.ads.domain.repository

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable

interface AdvertiserRepository {
    suspend fun get(id: String): Ads

    suspend fun getAll(filter: Filter = Filter(), page: Pageable): Page<Ads>

    suspend fun getMetrics(filter: Filter = Filter()): Metrics

    suspend fun create(id: String)

    suspend fun description(): Description
}
