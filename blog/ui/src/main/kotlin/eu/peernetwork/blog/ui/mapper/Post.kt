package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPostDetail
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.core.ui.mapper.annotate

fun Content.mapFromDomain(): UiPost {
    return UiPost(
        id = id,
        type = type.mapFromDomain(),
        author = author.mapFromDomain(),
        title = title.annotate(),
        description = description.annotate(),
        asset = media.mapFromDomain(),
        likes = likes,
        isLiked = isLiked,
        isDisliked = isDisliked,
        isViewed = isViewed,
        dislikes = dislikes,
        isAccessible = isAccessible,
        views = views,
        comment = comment,
        reported = reported,
        status = status.mapFromDomain(),
        url = url,
        time = createdAt.mapFromDomain(),
        createdAt = createdAt
    )
}

fun Content.Type.mapFromDomain(): UiPostType {
    return when(this) {
        Content.Type.TEXT -> UiPostType.TEXT
        Content.Type.IMAGE -> UiPostType.IMAGE
        Content.Type.AUDIO -> UiPostType.AUDIO
        Content.Type.VIDEO -> UiPostType.VIDEO
    }
}

fun UiPost.mapToDetail(): UiPostDetail {
    return UiPostDetail(
        id = id,
        uuid = author.id,
        slug = "#${author.slug}",
        username = author.username,
        title = title,
        description = description,
        imageUrl = author.imageUrl,
        time = time,
        reported = reported,
        isAccessible = isAccessible,
    )
}
