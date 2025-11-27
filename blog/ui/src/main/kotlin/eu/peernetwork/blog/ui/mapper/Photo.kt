package eu.peernetwork.blog.ui.mapper

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import kotlinx.collections.immutable.persistentListOf

fun Content.mapToPhoto(context: Context, annotate: (String) -> AnnotatedString): UiPost {
    return UiPost(
        id = id,
        title = annotate(title),
        description = annotate(description),
        media = persistentListOf(),
        author = UiAuthor(
            id = author.id,
            username = author.username,
            slug = author.slug,
            imageUrl = author.imageUrl,
            following = author.isfollowing,
            followed = author.isfollowed
        ),
        type = type.mapFromDomain(),
        aspectRatio = .4f,
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
