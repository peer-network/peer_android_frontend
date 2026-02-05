package eu.peernetwork.wallet.domain.model

import java.math.BigDecimal

data class Amount(
    val net: BigDecimal,
    val gross: BigDecimal,
)
