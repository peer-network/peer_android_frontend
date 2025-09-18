package eu.peernetwork.blog.ui.mapper

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.model.UiContent

fun Comment.mapToComment(annotate: (String) -> AnnotatedString): UiComment {
    return UiComment(
        id = id,
        author = author.mapFromDomain(),
        content = annotate(content),
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked
    )
}

fun UiComment.mapToContent(): UiContent {
    return UiContent(
        id = id,
        title = buildAnnotatedString { append(author.username) },
        description = content,
        author = author,
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isViewed = true,
        isDisliked = false,
        dislikes = 0,
        views = 2,
        comment = 0,
        url = ""
    )
}
