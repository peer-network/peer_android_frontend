package eu.peernetwork.ads.domain.model

import java.math.BigDecimal

data class Order(
    val start: Long,
    val end: Long,
    val plan: Ads.Plan,
    val token: BigDecimal
)
