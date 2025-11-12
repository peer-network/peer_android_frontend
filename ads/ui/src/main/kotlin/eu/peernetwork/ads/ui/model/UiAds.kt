package eu.peernetwork.ads.ui.model

import java.math.BigDecimal

data class UiAds(
    val from: Long,
    val to: Long,
    val status: Boolean,
    val cost: BigDecimal,
    val earning: BigDecimal,
    val content: UiContent
)
