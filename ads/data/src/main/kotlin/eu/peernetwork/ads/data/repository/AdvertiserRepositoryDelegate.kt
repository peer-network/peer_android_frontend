package eu.peernetwork.ads.data.repository

import eu.peernetwork.ads.data.api.AdvertiserApi
import eu.peernetwork.ads.domain.model.Campaign
import eu.peernetwork.ads.domain.model.AdsList
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.paging.Pageable
import javax.inject.Inject

class AdvertiserRepositoryDelegate @Inject constructor(
    private val api: AdvertiserApi
) : AdvertiserRepository {
    override suspend fun get(id: String): Campaign {
        return api.get(id)
    }

    override suspend fun getAll(filter: Filter, page: Pageable): AdsList {
        return api.getAll(filter, page)
    }

    override suspend fun getMetrics(filter: Filter): Metrics {
        return api.getMetrics(filter)
    }

    override suspend fun create(id: String) {
        return api.create(id)
    }

    override suspend fun description(): Description {
        return api.description()
    }
}
