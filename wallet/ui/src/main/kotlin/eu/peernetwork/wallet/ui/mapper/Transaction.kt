package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.core.ui.mapper.annotate
import eu.peernetwork.wallet.domain.model.Transaction
import eu.peernetwork.wallet.ui.model.UiTax
import eu.peernetwork.wallet.ui.model.UiTransaction

fun Transaction.mapToTransaction(): UiTransaction {
    return UiTransaction(
        id = id,
        res = category.toResource(),
        icon = category.toIcon(),
        sender = sender.mapFromDomain(),
        recipient = recipient.mapFromDomain(),
        message = message?.annotate(),
        category = category.mapFromDomain(),
        amount = amount.mapFromDomain(),
        fees = fees?.mapFromDomain(),
        tax = UiTax.Free,
        createdAt = createdAt
    )
}
