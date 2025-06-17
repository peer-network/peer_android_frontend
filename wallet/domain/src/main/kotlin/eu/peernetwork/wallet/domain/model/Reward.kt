package eu.peernetwork.wallet.domain.model

data class Reward(
    val type: String,
    val used: Int,
    val available: Int
)
