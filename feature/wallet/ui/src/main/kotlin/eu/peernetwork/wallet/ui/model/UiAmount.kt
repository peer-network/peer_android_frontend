package eu.peernetwork.wallet.ui.model

import java.math.BigDecimal

data class UiAmount(
    val net: BigDecimal,
    val gross: BigDecimal,
)
