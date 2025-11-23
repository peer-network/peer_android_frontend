package eu.peernetwork.ads.ui.model

import java.math.BigDecimal

data class UiAds(
    val id: String,
    val from: String,
    val to: String,
    val status: Boolean,
    val cost: BigDecimal,
    val earning: BigDecimal,
    val content: UiContent,
)
