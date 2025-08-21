package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement

fun UiContent.mapToEngagement(): UiEngagement {
    return UiEngagement(
        id = id,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        comment = comment,
        views = views
    )
}
