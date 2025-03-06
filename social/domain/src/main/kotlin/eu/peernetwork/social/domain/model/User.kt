package eu.peernetwork.social.domain.model

data class User(
    val id: String,
    val username: String,
    val bio: String,
    val imageUrl: String,
)
