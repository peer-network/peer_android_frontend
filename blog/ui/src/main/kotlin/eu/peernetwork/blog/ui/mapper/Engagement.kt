package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiPost

fun UiPost.mapToEngagement(): UiEngagement {
    return UiEngagement(
        id = id,
        likes = likes.toString(),
        dislikes = dislikes.toString(),
        isLiked = isLiked,
        isDisliked = isDisliked,
        views = views.toString(),
        comment = comment.toString()
    )
}
