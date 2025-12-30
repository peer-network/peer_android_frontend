package eu.peernetwork.user.domain.model

data class Account(
    val id: String,
    val slug: Int,
    val username: String,
    val bio: String,
    val imageUrl: String,
    val overview: Overview,
    val following: Boolean,
    val followed: Boolean,
    val reported: Boolean = false
)
