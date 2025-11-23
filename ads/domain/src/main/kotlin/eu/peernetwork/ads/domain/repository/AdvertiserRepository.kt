package eu.peernetwork.ads.domain.repository

import eu.peernetwork.ads.domain.model.Campaign
import eu.peernetwork.ads.domain.model.AdsList
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.core.common.paging.Pageable

interface AdvertiserRepository {
    suspend fun get(id: String): Campaign

    suspend fun getAll(filter: Filter = Filter(), page: Pageable): AdsList

    suspend fun getMetrics(filter: Filter = Filter()): Metrics

    suspend fun create(id: String)

    suspend fun description(): Description
}
