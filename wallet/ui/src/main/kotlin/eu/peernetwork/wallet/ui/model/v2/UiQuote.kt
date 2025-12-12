package eu.peernetwork.wallet.ui.model.v2

import java.math.BigDecimal

data class UiQuote(
    val value: BigDecimal,
    val available: Int,
    val balance: BigDecimal
)
