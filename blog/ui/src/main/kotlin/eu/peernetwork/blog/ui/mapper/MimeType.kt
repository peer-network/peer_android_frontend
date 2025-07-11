package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.media.core.model.UiMimeType

fun UiMimeType.query(): String {
    return "?query=$id"
}
