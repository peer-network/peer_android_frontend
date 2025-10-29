package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.AdvertiserApi
import eu.peernetwork.blog.domain.model.AdPlan
import eu.peernetwork.blog.domain.model.Advert
import eu.peernetwork.blog.domain.repository.AdvertiserRepository
import javax.inject.Inject

class AdvertiserRepositoryDelegate @Inject constructor(
    private val api: AdvertiserApi
) : AdvertiserRepository {
    override suspend fun create(
        id: String,
        ad: AdPlan,
        refresh: Boolean
    ): Advert {
        return api.create(id, ad, refresh)
    }
}
