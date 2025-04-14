package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiComment

fun Comment.mapToComment(): UiComment {
    return UiComment(
        id = id,
        author = author.mapFromDomain(),
        content = content,
        createdAt = createdAt,
        likes = likes,
    )
}