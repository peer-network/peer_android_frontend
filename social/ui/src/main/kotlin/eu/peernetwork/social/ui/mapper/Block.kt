package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Block
import eu.peernetwork.social.ui.model.UiBlock

fun Block.mapFromDomain(): UiBlock {
    return UiBlock(
        userId = userId,
        username = username,
        slug = slug,
        image = image
    )
}