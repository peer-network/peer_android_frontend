package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.ui.model.UiAuthor

fun Author.mapFromDomain(): UiAuthor {
    return UiAuthor(
        id = id,
        username = username,
        slug = slug,
        imageUrl = imageUrl
    )
}
