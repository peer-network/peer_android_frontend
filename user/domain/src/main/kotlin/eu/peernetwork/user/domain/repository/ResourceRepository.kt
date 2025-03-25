package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Coupon

interface ResourceRepository {
    suspend fun coupons(): List<Coupon>

    suspend fun string(path: String): String
}
