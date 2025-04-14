package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.blog.ui.model.UiPoint

fun Point.mapFromDomain(): UiPoint {
    return UiPoint(
        name = type,
        used = used,
        available = available
    )
}
