package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Content.Type
import eu.peernetwork.blog.domain.model.Draft
import type.ContenType
import type.FilterType

fun Type.mapToFilter(): FilterType {
    return when(this) {
        Type.TEXT -> FilterType.TEXT
        Type.AUDIO -> FilterType.AUDIO
        Type.IMAGE -> FilterType.IMAGE
        Type.VIDEO -> FilterType.VIDEO
    }
}

fun Draft.Type.mapFromDomain(): ContenType {
    return when(this) {
        is Draft.Type.Video -> ContenType.video
        is Draft.Type.Audio -> ContenType.audio
        is Draft.Type.Image -> ContenType.image
        is Draft.Type.Text -> ContenType.text
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