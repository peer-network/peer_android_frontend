package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Fees
import eu.peernetwork.wallet.ui.model.UiFees

fun Fees.mapFromDomain(): UiFees {
    return UiFees(
        total = total,
        burn = burn,
        peer = peer,
        commission = commission
    )
}
