package eu.peernetwork.ads.remote.mapper

import ads.type.ContentVisibilityStatus
import eu.peernetwork.ads.domain.model.Status

fun ContentVisibilityStatus.mapToDomain(): Status {
    return when (rawValue) {
        ContentVisibilityStatus.ILLEGAL.rawValue -> Status.ILLEGAL
        ContentVisibilityStatus.HIDDEN.rawValue -> Status.HIDDEN
        else -> Status.VISIBLE
    }
}
