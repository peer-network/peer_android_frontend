package eu.peernetwork.blog.data.mapper

import eu.peernetwork.user.domain.model.Status

fun Status.mapToDomain(): eu.peernetwork.blog.domain.model.Status {
    return when (this) {
        Status.VISIBLE -> eu.peernetwork.blog.domain.model.Status.VISIBLE
        Status.ILLEGAL -> eu.peernetwork.blog.domain.model.Status.ILLEGAL
        Status.HIDDEN -> eu.peernetwork.blog.domain.model.Status.HIDDEN
    }
}
