package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.ui.mapper.annotate

fun Comment.mapToComment(): UiComment {
    return UiComment(
        id = id,
        author = author.mapFromDomain(),
        content = content.annotate(),
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isReported = isReported,
        isAccessible = isAccessible,
        status = status.mapFromDomain()
    )
}
