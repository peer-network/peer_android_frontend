package eu.peernetwork.wallet.domain.model

data class User(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val following: Boolean,
    val followed: Boolean,
    val isAccessible: Boolean,
    val status: Status,
    val timestamp: Long
)
