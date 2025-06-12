package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.ui.model.UiTransfer

fun Receipt.mapFromDomain(): UiTransfer {
    return UiTransfer(
        recipient = recipient,
        token = price
    )
}
