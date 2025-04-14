package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(): UiVideo {
    val media = media.first()
    return UiVideo(
        id = id,
        title = title,
        description = "",
        media = media.path,
        author = author.mapFromDomain(),
        createdAt = createdAt,
        likes = likes,
        dislikes = dislikes,
        comment = comment,
        resolution = media.options.resolution
    )
}
