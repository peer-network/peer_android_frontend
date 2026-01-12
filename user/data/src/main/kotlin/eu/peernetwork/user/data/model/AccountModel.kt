package eu.peernetwork.user.data.model

import eu.peernetwork.user.domain.model.Status

data class AccountModel(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val biography: String,
    val followed: Int,
    val follower: Int,
    val isfollowing: Boolean,
    val isfollowed: Boolean,
    val status: Status,
    val hasActiveReports: Boolean = false,
    val posts: Int,
    val peers: Int
)
