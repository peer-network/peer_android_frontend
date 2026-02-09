package eu.peernetwork.wallet.domain.model

data class Tax(
    val burn: Double,
    val pool: Double,
    val peer: Double,
    val percentage: Double
)
