package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiEngagement
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
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        comment = comment,
        resolution = media.options.resolution
    )
}

fun UiVideo.mapToEngagement(): UiEngagement {
    return UiEngagement(
        id = id,
        likes = likes,
        dislikes = dislikes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        comment = comment
    )
}
