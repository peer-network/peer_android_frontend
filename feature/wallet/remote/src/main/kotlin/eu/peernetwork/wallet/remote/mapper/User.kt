package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.User
import wallet.wallet.eu.peernetwork.wallet.remote.TransactionHistoryQuery

fun TransactionHistoryQuery.Sender.mapToDomain(): User {
    return User(
        id = userid ?: "",
        slug = slug ?: 0,
        username = username ?: "",
        imageUrl = img ?: "",
        status = visibilityStatus.mapToDomain(),
        isAccessible = !isHiddenForUsers,
    )
}

fun TransactionHistoryQuery.Recipient.mapToDomain(): User {
    return User(
        id = userid ?: "",
        slug = slug ?: 0,
        username = username ?: "",
        imageUrl = img ?: "",
        status = visibilityStatus.mapToDomain(),
        isAccessible = !isHiddenForUsers,
    )
}
