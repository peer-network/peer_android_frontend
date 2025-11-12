package eu.peernetwork.ads.remote.model

import java.math.BigDecimal

data class AdsModel(
    val from: Long,
    val to: Long,
    val status: Boolean,
    val cost: BigDecimal,
    val earning: BigDecimal,
)
