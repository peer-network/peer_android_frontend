package eu.peernetwork.wallet.remote.mapper

import java.math.BigDecimal

fun Any?.toBigDecimalOrNull(): BigDecimal? = BigDecimal(this.toString())
