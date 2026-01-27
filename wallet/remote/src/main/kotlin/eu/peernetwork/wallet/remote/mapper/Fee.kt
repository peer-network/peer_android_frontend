package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Fees
import wallet.wallet.eu.peernetwork.wallet.remote.TransactionHistoryQuery
import java.math.BigDecimal

fun TransactionHistoryQuery.Fees.mapFromDomain(): Fees {
    return Fees(
        total = total.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        burn = burn.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        peer = peer.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        commission = inviter?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
    )
}
