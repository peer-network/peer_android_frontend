package eu.peernetwork.wallet.domain.model

import java.math.BigDecimal

data class Fee(
    val total: BigDecimal,
    val burn: BigDecimal,
    val peer: BigDecimal,
    val commission: BigDecimal,
)
