package eu.peernetwork.blog.ui.mapper.v2

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.blog.ui.model.v2.UiPostType
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
        views = views,
        comment = comment,
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
        slug = "#${author.slug}",
        username = author.username,
        title = title,
        description = description,
        imageUrl = author.imageUrl,
        time = time,
    )
}
