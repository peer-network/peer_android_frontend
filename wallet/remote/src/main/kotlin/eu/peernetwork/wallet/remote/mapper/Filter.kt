package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Transaction
import wallet.type.TokenMovementFilterType

fun Filter.mapToType(): TokenMovementFilterType? {
    return type?.let {
        when(it) {
            Transaction.Type.TRANSACTION -> TokenMovementFilterType.TRANSACTION
            Transaction.Type.AIRDROP -> TokenMovementFilterType.AIRDROP
            Transaction.Type.PAYMENT -> TokenMovementFilterType.PAYMENT
            Transaction.Type.BURN -> TokenMovementFilterType.BURN
            Transaction.Type.MINT -> TokenMovementFilterType.MINT
        }
    }
}
