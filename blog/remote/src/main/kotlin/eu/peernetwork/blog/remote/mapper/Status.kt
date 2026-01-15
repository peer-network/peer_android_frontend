package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Status
import type.ContentVisibilityStatus

fun ContentVisibilityStatus.mapToDomain(): Status {
    return when (this.rawValue) {
        ContentVisibilityStatus.ILLEGAL.rawValue -> Status.ILLEGAL
        ContentVisibilityStatus.HIDDEN.rawValue -> Status.HIDDEN
        else -> Status.VISIBLE
    }
}
