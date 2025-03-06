package eu.peernetwork.user.domain.model

data class Profile(
    val username: String,
    val email: String,
    val bio: String,
    val imageUrl: String,
)
