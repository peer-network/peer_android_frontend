package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiPost

fun Content.mapToPhoto(): UiPost {
    return UiPost(
        id = id,
        title = title,
        description = description,
        media = media.map { it.mapFromDomain() },
        author = author.mapFromDomain(),
        type = type.mapFromDomain(),
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        comment = comment
    )
}

fun Content.Type.mapFromDomain():  UiPost.Type {
    return when(this) {
        Content.Type.TEXT -> UiPost.Type.TEXT
        Content.Type.IMAGE -> UiPost.Type.IMAGE
        else -> UiPost.Type.TEXT
    }
}

fun UiPost.mapToContent(): UiContent {
    return UiContent(
        id = id,
        title = title,
        description = description,
        author = author,
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        comment = comment
    )
}

fun UiPost.mapToEngagement(): UiEngagement {
    return UiEngagement(
        id = id,
        likes = likes,
        dislikes = dislikes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        comment = comment
    )
}
