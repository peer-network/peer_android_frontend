package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Amount
import eu.peernetwork.wallet.ui.model.UiAmount
import java.math.RoundingMode

fun Amount.mapFromDomain(): UiAmount {
    return UiAmount(
        net = net.setScale(8, RoundingMode.HALF_UP),
        gross = gross.setScale(8, RoundingMode.HALF_UP)
    )
}
