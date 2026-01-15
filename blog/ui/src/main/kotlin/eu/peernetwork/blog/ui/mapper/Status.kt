package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Status
import eu.peernetwork.blog.ui.model.UiStatus

fun Status.mapFromDomain(): UiStatus {
    return when (this) {
        Status.VISIBLE -> UiStatus.VISIBLE
        Status.HIDDEN -> UiStatus.HIDDEN
        Status.ILLEGAL -> UiStatus.ILLEGAL
    }
}
