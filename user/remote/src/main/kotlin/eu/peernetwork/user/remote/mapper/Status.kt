package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Status
import protected.type.ContentVisibilityStatus

fun ContentVisibilityStatus.mapToDomain(): Status {
    return when (this) {
        ContentVisibilityStatus.HIDDEN -> Status.HIDDEN
        ContentVisibilityStatus.ILLEGAL -> Status.ILLEGAL
        else -> Status.VISIBLE
    }
}
