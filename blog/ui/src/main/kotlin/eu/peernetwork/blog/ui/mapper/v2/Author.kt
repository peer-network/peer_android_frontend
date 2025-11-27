package eu.peernetwork.blog.ui.mapper.v2

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.ui.model.v2.UiAuthor

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
