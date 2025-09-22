package eu.peernetwork.blog.ui.mapper

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(context: Context, annotate: (String) -> AnnotatedString): UiVideo {
    val media = media.first()
    return UiVideo(
        id = id,
        title = annotate(title),
        description = annotate(description),
        media = media.path,
        author = author.mapFromDomain(),
        time = context.timeAgo(createdAt, System.currentTimeMillis()),
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        views = views,
        isViewed = isViewed,
        comment = comment,
        aspectRatio = media.getAspectRatio(),
        resolution = media.options.resolution,
        url = url
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
        isViewed = isViewed,
        views = views,
        comment = comment,
        url = url
    )
}

fun UiPost.mapToVideo(): UiVideo {
    val media = media.first()
    return UiVideo(
        id = id,
        title = title,
        description = description,
        author = author,
        createdAt = createdAt,
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        dislikes = dislikes,
        isViewed = isViewed,
        views = views,
        comment = comment,
        aspectRatio = aspectRatio,
        resolution = media.options.resolution,
        time = time,
        media = media.path,
        url = url
    )
}
