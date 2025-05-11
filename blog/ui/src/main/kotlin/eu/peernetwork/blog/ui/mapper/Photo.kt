package eu.peernetwork.blog.ui.mapper

import androidx.compose.ui.text.AnnotatedString
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiPost

fun Content.mapToPhoto(annotate: (String) -> AnnotatedString): UiPost {
    return UiPost(
        id = id,
        title = annotate(title),
        description = annotate(description),
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
