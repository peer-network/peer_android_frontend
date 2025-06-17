package eu.peernetwork.wallet.domain.model

import java.math.BigDecimal

data class Receipt (
    val recipient: String,
    val price: BigDecimal
)
