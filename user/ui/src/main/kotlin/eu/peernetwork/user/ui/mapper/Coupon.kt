package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.domain.model.Point
import eu.peernetwork.user.ui.model.UiPoint

fun Point.mapFromDomain(): UiPoint {
    return UiPoint(
        name = type,
        used = used,
        available = available
    )
}
