package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.media.core.model.Property

fun Media.mapFromDomain(): UiMedia {
    return UiMedia(
        path = path,
        options = UiMedia.Options(
            size = options.size,
            resolution = options.resolution
        )
    )
}

fun UiMedia.mapToProperty(): Property {
    return Property(
        size = options.size,
        description = path,
        resolution = options.resolution
    )
}
