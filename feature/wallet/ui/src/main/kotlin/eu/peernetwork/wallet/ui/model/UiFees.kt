package eu.peernetwork.wallet.ui.model

import java.math.BigDecimal

data class UiFees(
    val total: BigDecimal,
    val burn: BigDecimal,
    val peer: BigDecimal,
    val commission: BigDecimal,
)
