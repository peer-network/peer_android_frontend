package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.ui.model.UiQuote

fun Quote.mapFromDomain(): UiQuote {
    return UiQuote(value)
}
