package eu.peernetwork.user.domain.model

data class Overview(
    val posts: Int,
    val peers: Int,
    val followers: Int,
    val followed: Int,
)
