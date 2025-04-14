package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiPost

fun Content.mapToPhoto(): UiPost {
    return UiPost(
        id = id,
        title = title,
        description = description,
        media = media.map { it.mapFromDomain() },
        author = author.mapFromDomain(),
        type = type.mapFromDomain(),
        createdAt = createdAt,
        likes = likes,
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
