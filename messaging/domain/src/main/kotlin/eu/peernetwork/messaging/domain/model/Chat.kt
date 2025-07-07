package eu.peernetwork.messaging.domain.model

data class Chat(
    val id: String,
    val name: String,
    val image: String? = null,
    val createdAt: String
)
