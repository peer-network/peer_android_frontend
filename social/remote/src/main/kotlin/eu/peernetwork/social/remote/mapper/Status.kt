package eu.peernetwork.social.remote.mapper

import eu.peernetwork.social.domain.model.Status
import social.type.ContentVisibilityStatus

fun ContentVisibilityStatus.mapToDomain(): Status {
    return when (rawValue) {
        ContentVisibilityStatus.ILLEGAL.rawValue -> Status.ILLEGAL
        ContentVisibilityStatus.HIDDEN.rawValue -> Status.HIDDEN
        else -> Status.VISIBLE
    }
}
