package eu.peernetwork.user.domain.model

data class User(
    val id: String,
    val slug: Int,
    val username: String,
    val bio: String,
    val imageUrl: String
)
