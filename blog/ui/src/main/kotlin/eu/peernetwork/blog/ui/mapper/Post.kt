package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.ui.model.UiPost

fun UiPost.mapToDetail(): UiPost.Detail {
    return UiPost.Detail(
        slug = "#${author.slug}",
        username = author.username,
        title = title,
        description = description,
        imageUrl = author.imageUrl,
        time = time
    )
}

fun UiPost.mapToEngagement(): UiPost.Engagement {
    return UiPost.Engagement(
        id = id,
        likes = likes.toString(),
        dislikes = likes.toString(),
        isLiked = isLiked,
        isDisliked = isDisliked,
        views = views.toString(),
        comment = comment.toString()
    )
}
