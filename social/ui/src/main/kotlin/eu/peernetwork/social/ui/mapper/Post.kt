package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.model.UiPost

fun Post.mapFromDomain(): UiPost {
    return UiPost(
        id = id,
        type = type,
        title = title,
        description = description,
        author = UiMember(
            id = author.id,
            username = author.username,
            imageUrl = author.imageUrl
        )
    )
}
