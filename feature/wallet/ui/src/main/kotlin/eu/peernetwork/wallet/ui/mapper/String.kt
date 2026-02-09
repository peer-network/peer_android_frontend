package eu.peernetwork.wallet.ui.mapper

import java.math.BigDecimal

fun BigDecimal.format(): String {
    if (this.compareTo(BigDecimal.ZERO) == 0) {
        return "0.00"
    }
    return this.stripTrailingZeros()
        .toPlainString()
}
