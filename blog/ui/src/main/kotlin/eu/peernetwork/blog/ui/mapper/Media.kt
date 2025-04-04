package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.ui.model.UiMedia

fun Media.mapFromDomain(): UiMedia {
    return UiMedia(
        path = path,
        options = UiMedia.Options(size = options.size)
    )
}
