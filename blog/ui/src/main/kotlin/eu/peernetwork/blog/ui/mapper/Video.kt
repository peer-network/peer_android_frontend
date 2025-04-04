package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(): UiVideo {
    return UiVideo(
        id = id,
        title = title,
        description = description,
        media = media,
        author = author.mapFromDomain(),
        createdAt = createdAt
    )
}
