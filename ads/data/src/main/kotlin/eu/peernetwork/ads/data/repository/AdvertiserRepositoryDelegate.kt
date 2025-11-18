package eu.peernetwork.ads.data.repository

import eu.peernetwork.ads.data.api.AdvertiserApi
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import javax.inject.Inject

class AdvertiserRepositoryDelegate @Inject constructor(
    private val api: AdvertiserApi
) : AdvertiserRepository {
    override suspend fun get(id: String): Ads {
        return api.get(id)
    }

    override suspend fun getAll(filter: Filter, page: Pageable): Page<Ads> {
        return api.getAll(filter, page)
    }

    override suspend fun getMetrics(author: String): Metrics {
        return api.getMetrics(author)
    }

    override suspend fun create(id: String) {
        return api.create(id)
    }
}
