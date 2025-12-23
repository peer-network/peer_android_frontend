package eu.peernetwork.app.mapper

import eu.peernetwork.ads.ui.model.UiCharge
import eu.peernetwork.wallet.ui.model.UiTax

fun UiTax.mapToCharges(): UiCharge {
    return UiCharge(
        burn = burn,
        pool = pool,
        peer = peer,
        percentage = percentage
    )
}
