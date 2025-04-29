package eu.peernetwork.social.domain.model

data class Member(
    val id: String,
    val username: String,
    val bio: String,
    val imageUrl: String,
)
