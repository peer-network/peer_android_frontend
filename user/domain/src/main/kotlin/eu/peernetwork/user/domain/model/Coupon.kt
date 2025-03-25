package eu.peernetwork.user.domain.model

data class Coupon(
    val name: String,
    val used: Int,
    val available: Int
)
