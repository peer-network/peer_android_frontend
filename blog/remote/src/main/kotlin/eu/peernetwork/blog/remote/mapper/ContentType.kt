package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Content.Type
import eu.peernetwork.blog.domain.model.Draft
import type.ContentType
import type.PostFilterType

fun Type.mapToFilter(): PostFilterType {
    return when(this) {
        Type.TEXT -> PostFilterType.TEXT
        Type.AUDIO -> PostFilterType.AUDIO
        Type.IMAGE -> PostFilterType.IMAGE
        Type.VIDEO -> PostFilterType.VIDEO
    }
}

fun Category.mapToFilter(): PostFilterType? {
    return when(this) {
        Category.FOLLOWED -> PostFilterType.FOLLOWED
        Category.FOLLOWER -> PostFilterType.FOLLOWER
        else -> null
    }
}

fun Type.mapToContentType(): ContentType {
    return when(this) {
        Type.TEXT -> ContentType.text
        Type.AUDIO -> ContentType.audio
        Type.IMAGE -> ContentType.image
        Type.VIDEO -> ContentType.video
    }
}

fun Draft.Type.mapFromDomain(): ContentType {
    return when(this) {
        is Draft.Type.Video -> ContentType.video
        is Draft.Type.Audio -> ContentType.audio
        is Draft.Type.Image -> ContentType.image
        is Draft.Type.Text -> ContentType.text
    }
}

fun String.mapToDomain(): Type {
    return when(this) {
        "video" -> Type.VIDEO
        "audio" -> Type.AUDIO
        "image" -> Type.IMAGE
        else -> Type.TEXT
    }
}