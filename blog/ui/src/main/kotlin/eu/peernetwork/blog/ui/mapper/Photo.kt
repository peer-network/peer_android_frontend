package eu.peernetwork.blog.ui.mapper

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiPost
import kotlinx.collections.immutable.toPersistentList

fun Content.mapToPhoto(context: Context, annotate: (String) -> AnnotatedString): UiPost {
    val media = media.map { it.mapFromDomain() }.toPersistentList()
    return UiPost(
        id = id,
        title = annotate(title),
        description = annotate(description),
        media = media,
        author = author.mapFromDomain(),
        type = type.mapFromDomain(),
        aspectRatio = media.getAspectRatio(),
        time = context.timeAgo(createdAt, System.currentTimeMillis()),
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

fun Content.Type.mapFromDomain():  UiPost.Type {
    return when(this) {
        Content.Type.TEXT -> UiPost.Type.TEXT
        Content.Type.IMAGE -> UiPost.Type.IMAGE
        Content.Type.AUDIO -> UiPost.Type.AUDIO
        Content.Type.VIDEO -> UiPost.Type.VIDEO
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
        views = views,
        isViewed = isViewed,
        comment = comment,
        url = url
    )
}
