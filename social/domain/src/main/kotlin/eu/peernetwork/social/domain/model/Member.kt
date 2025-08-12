package eu.peernetwork.social.domain.model

data class Member(
    val id: String,
    val slug: String,
    val username: String,
    val imageUrl: String,
    val isFollowed: Boolean,
    val isFollowing: Boolean,
)
