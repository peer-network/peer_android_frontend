package eu.peernetwork.wallet.remote.mapper

import java.math.BigDecimal

fun Any?.toBigDecimalOrNull(): BigDecimal? {
    if (this == null) return null
    val value = this.toString().trim()
    if (value.isEmpty()
        || value.equals("null", ignoreCase = true)
        || value.equals("nan", ignoreCase = true)) {
        return null
    }
    return runCatching { BigDecimal(value) }.getOrNull()
}
