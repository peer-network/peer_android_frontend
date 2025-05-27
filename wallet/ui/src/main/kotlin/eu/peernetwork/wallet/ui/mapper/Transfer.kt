package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Transfer
import eu.peernetwork.wallet.ui.model.UiTransfer

fun Transfer.mapFromDomain(): UiTransfer {
    return UiTransfer(
        recipient = recepient,
        numberOfToken = numberoftokens
    )
}