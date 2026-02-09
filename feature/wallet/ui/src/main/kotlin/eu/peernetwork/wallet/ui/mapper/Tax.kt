package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.ui.model.UiTax

fun Tax.mapFromDomain(): UiTax {
    return UiTax(
        burn = burn,
        pool = pool,
        peer = peer,
        percentage = percentage
    )
}
