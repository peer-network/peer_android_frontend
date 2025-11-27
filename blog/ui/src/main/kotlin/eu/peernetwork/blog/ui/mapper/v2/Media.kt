package eu.peernetwork.blog.ui.mapper.v2

import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.ui.model.v2.UiAsset
import eu.peernetwork.blog.ui.model.v2.UiDisplay
import eu.peernetwork.blog.ui.model.v2.UiMedia
import kotlinx.collections.immutable.toPersistentList

fun Media.mapFromDomain(): UiMedia {
    return UiMedia(
        path = path,
        display = UiDisplay(
            size = options.size,
            cover = options.cover,
            resolution = options.resolution
        )
    )
}

fun List<Media>.mapFromDomain(): UiAsset {
    val media = map { it.mapFromDomain() }
    return UiAsset(
        ratio = media.getAspectRatio(),
        media = media.toPersistentList()
    )
}

fun UiMedia.getAspectRatio(): Float {
    return (display.resolution?.let {
        it.first.toFloat() / it.second.toFloat()
    } ?: 1f).coerceIn(0.8f, 1f)
}

fun List<UiMedia>.getAspectRatio(): Float {
    if (size == 1) {
        return first().getAspectRatio()
    }
    return (minOfOrNull { media ->
        media.display.resolution?.let { media.getAspectRatio() } ?: 1f
    } ?: 1f).coerceIn(0.8f, 1f)
}
