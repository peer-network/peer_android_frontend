package eu.peernetwork.messaging.ui.mapper

import eu.peernetwork.messaging.domain.model.Message
import eu.peernetwork.messaging.ui.model.UiMessage

fun Message.mapFromDomain(): UiMessage {
    return UiMessage(messId, chatId, userId, content, createdAt)
}