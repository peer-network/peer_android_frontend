package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Coupon

interface ResourceApi {
    suspend fun coupons(): List<Coupon>

    suspend fun string(path: String): String
}
