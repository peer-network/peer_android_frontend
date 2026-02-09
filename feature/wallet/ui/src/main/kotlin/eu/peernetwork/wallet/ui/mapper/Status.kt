package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Status
import eu.peernetwork.wallet.ui.model.UiStatus

fun Status.mapFromDomain(): UiStatus {
    return when (this) {
        Status.VISIBLE -> UiStatus.VISIBLE
        Status.HIDDEN -> UiStatus.HIDDEN
        Status.ILLEGAL -> UiStatus.ILLEGAL
    }
}
