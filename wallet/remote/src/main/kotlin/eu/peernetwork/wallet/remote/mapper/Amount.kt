package eu.peernetwork.wallet.remote.mapper

import java.math.BigDecimal

fun String.mapToAmount(): BigDecimal = BigDecimal(this)
