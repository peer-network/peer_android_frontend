package eu.peernetwork.blog.ui.mapper

import androidx.compose.ui.text.AnnotatedString
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(annotate: (String) -> AnnotatedString): UiVideo {
    val media = media.first()
    return UiVideo(
        id = id,
        title = annotate(title),
        description = annotate(description),
        media = media.path,
        author = author.mapFromDomain(),
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        comment = comment,
        resolution = media.options.resolution
    )
}

fun UiVideo.mapToContent(): UiContent {
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

fun UiVideo.mapToEngagement(): UiEngagement {
    return UiEngagement(
        id = id,
        likes = likes,
        dislikes = dislikes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        comment = comment
    )
}
