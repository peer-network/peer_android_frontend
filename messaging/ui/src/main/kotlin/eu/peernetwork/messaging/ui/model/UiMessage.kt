package eu.peernetwork.messaging.ui.model

data class UiMessage(
    val messId: Int,
    val chatId: String,
    val userId: String,
    val content: String,
    val createdAt: String
)