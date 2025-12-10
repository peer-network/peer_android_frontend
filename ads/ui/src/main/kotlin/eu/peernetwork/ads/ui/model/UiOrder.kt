package eu.peernetwork.ads.ui.model

import java.math.BigDecimal

data class UiOrder(
    val start: String,
    val duration: Int,
    val price: BigDecimal,
    val token: BigDecimal
)
