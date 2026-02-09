package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Status
import eu.peernetwork.ads.ui.model.UiStatus

fun Status.mapFromDomain(): UiStatus {
    return when (this) {
        Status.VISIBLE -> UiStatus.VISIBLE
        Status.HIDDEN -> UiStatus.HIDDEN
        Status.ILLEGAL -> UiStatus.ILLEGAL
    }
}
