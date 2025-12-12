package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.ui.model.UiAuthor

fun Author.mapFromDomain(): UiAuthor {
    return UiAuthor(
        id = id,
        slug = slug,
        username = username,
        imageUrl = imageUrl,
        following = isfollowing,
        followed = isfollowed
    )
}
