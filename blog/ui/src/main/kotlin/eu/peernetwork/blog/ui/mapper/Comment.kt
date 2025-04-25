package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.model.UiContent

fun Comment.mapToComment(): UiComment {
    return UiComment(
        id = id,
        author = author.mapFromDomain(),
        content = content,
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked
    )
}

fun UiComment.mapToContent(): UiContent {
    return UiContent(
        id = id,
        title = author.username,
        description = content,
        author = author,
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = false,
        dislikes = 0,
        comment = 0
    )
}
