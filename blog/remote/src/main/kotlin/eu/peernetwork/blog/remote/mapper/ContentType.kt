package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.ContentType
import type.ContenType
import type.FilterType

fun ContentType.mapToFilter(): FilterType {
    return when(this) {
        ContentType.VIDEO -> FilterType.VIDEO
        ContentType.AUDIO -> FilterType.AUDIO
        ContentType.IMAGE -> FilterType.IMAGE
        ContentType.TEXT -> FilterType.TEXT
    }
}

fun ContenType.mapToDomain(): ContentType {
    return when(this) {
        ContenType.video -> ContentType.VIDEO
        ContenType.audio -> ContentType.AUDIO
        ContenType.image -> ContentType.IMAGE
        ContenType.text -> ContentType.TEXT
        else -> ContentType.TEXT
    }
}

fun ContentType.mapFromDomain(): ContenType {
    return when(this) {
        ContentType.VIDEO -> ContenType.video
        ContentType.AUDIO -> ContenType.audio
        ContentType.IMAGE -> ContenType.image
        ContentType.TEXT -> ContenType.text
    }
}

fun String.mapToDomain(): ContentType {
    return when(this) {
        "video" -> ContentType.VIDEO
        "audio" -> ContentType.AUDIO
        "image" -> ContentType.IMAGE
        "text" -> ContentType.TEXT
        else -> ContentType.TEXT
    }
}