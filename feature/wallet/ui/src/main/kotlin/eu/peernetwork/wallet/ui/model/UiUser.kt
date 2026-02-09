package eu.peernetwork.wallet.ui.model

data class UiUser(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val isAccessible: Boolean,
    val status: UiStatus
)
