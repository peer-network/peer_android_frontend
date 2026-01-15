package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.domain.model.Status
import eu.peernetwork.user.ui.model.UiStatus

fun Status.mapFromDomain(): UiStatus {
    return when (this) {
        Status.HIDDEN -> UiStatus.HIDDEN
        Status.ILLEGAL -> UiStatus.ILLEGAL
        else -> UiStatus.VISIBLE
    }
}
