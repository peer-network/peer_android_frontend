package eu.peernetwork.messaging.ui.model

data class UiChat(
    val id: String,
    val name: String,
    val image: String? = null,
    val createdAt: String
)