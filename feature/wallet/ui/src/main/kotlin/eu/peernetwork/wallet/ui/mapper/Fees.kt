package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Fees
import eu.peernetwork.wallet.ui.model.UiFees
import java.math.RoundingMode

fun Fees.mapFromDomain(): UiFees {
    return UiFees(
        total = total.setScale(8, RoundingMode.HALF_UP),
        burn = burn.setScale(8, RoundingMode.HALF_UP),
        peer = peer.setScale(8, RoundingMode.HALF_UP),
        commission = commission.setScale(8, RoundingMode.HALF_UP)
    )
}
