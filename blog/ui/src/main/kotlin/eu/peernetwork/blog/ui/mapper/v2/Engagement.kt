package eu.peernetwork.blog.ui.mapper.v2

import eu.peernetwork.blog.ui.model.v2.UiEngagement
import eu.peernetwork.blog.ui.model.v2.UiPost

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
