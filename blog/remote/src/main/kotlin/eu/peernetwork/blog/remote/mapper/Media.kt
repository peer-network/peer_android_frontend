package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.remote.model.MediaModel

fun MediaModel.mapFromDomain(): Media {
    return Media(
        path = path,
        options = Media.Options(
            size = options?.size ?: "",
            cover = options?.cover,
            resolution = options?.resolution?.mapToIntPair()
        )
    )
}

fun String.mapToIntPair(): Pair<Int, Int>? {
    return try {
        val (width, height) = split("x").map { it.toInt() }
        width to height
    } catch (error: Throwable) {
        error.printStackTrace()
        null
    }
}
