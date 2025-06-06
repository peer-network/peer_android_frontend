package eu.peernetwork.wallet.domain.model

import java.math.BigDecimal

data class Transfer (
    val recipient: String,
    val token: BigDecimal
)