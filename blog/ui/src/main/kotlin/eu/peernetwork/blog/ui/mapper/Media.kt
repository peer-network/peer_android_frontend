package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.ui.model.UiMedia

fun Media.mapFromDomain(): UiMedia {
    return UiMedia(
        path = path,
        options = UiMedia.Options(
            size = options.size,
            cover = options.cover,
            resolution = options.resolution
        )
    )
}

fun Media.getAspectRatio(default: Float = 1f): Float {
    return minOf(options.resolution?.let {
        (it.first.toFloat() / it.second.toFloat())
    } ?: default, default)
}

fun UiMedia.getAspectRatio(): Float {
    return options.resolution?.let {
        (it.first.toFloat() / it.second.toFloat())
    } ?: 1f
}

fun List<UiMedia>.getAspectRatio(): Float {
    if (size == 1) {
        return first().getAspectRatio()
    }
    return minOfOrNull { media ->
        when {
            media.options.resolution != null -> media.getAspectRatio()
            else -> 1f
        }
    } ?: 1f
}
