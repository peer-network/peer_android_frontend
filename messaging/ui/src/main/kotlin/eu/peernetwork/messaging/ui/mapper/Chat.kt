package eu.peernetwork.messaging.ui.mapper

import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.ui.model.UiChat

fun Chat.mapFromDomain(): UiChat {
    return UiChat(
        id = id,
        name = name,
        image = image,
        createdAt = createdAt
    )
}
