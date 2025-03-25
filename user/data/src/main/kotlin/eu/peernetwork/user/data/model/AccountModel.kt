package eu.peernetwork.user.data.model

data class AccountModel(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val biography: String,
    val followed: Int,
    val follower: Int,
    val posts: Int,
    val peers: Int
)
