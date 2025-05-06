package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Tag
import eu.peernetwork.social.ui.model.UiTag

fun Tag.mapFromDomain(): UiTag {
    return UiTag(value)
}
