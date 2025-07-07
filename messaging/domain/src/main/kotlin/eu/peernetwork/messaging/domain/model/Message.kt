package eu.peernetwork.messaging.domain.model

data class Message(
    val messId: Int,
    val chatId: String,
    val userId: String,
    val content: String,
    val createdAt: String
)