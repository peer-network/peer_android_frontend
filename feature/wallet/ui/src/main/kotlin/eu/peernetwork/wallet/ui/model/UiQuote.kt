package eu.peernetwork.wallet.ui.model

import java.math.BigDecimal

data class UiQuote(
    val price: BigDecimal,
    val available: Int,
    val balance: BigDecimal
)
