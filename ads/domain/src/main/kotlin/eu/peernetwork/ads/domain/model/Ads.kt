package eu.peernetwork.ads.domain.model

import java.math.BigDecimal

data class Ads(
    val from: Long,
    val to: Long,
    val status: Boolean,
    val cost: BigDecimal,
    val earning: BigDecimal,
    val content: Content
)
