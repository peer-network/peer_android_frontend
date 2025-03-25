package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.domain.model.Coupon
import eu.peernetwork.user.domain.repository.ResourceRepository
import javax.inject.Inject

class ResourceRepositoryDelegate @Inject constructor(
    private val api: ResourceApi
) : ResourceRepository {
    override suspend fun coupons(): List<Coupon> {
        return api.coupons()
    }

    override suspend fun string(path: String): String {
        return api.string(path)
    }
}
