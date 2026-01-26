package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Status
import wallet.type.ContentVisibilityStatus

fun ContentVisibilityStatus.mapToDomain(): Status {
    return when (this.rawValue) {
        ContentVisibilityStatus.ILLEGAL.rawValue -> Status.ILLEGAL
        ContentVisibilityStatus.HIDDEN.rawValue -> Status.HIDDEN
        else -> Status.VISIBLE
    }
}

