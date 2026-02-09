package eu.peernetwork.user.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiAccount(
    val id: String,
    val slug: Int,
    val username: String,
    val bio: String?,
    val imageUrl: String,
    val metric: UiMetric,
    val isFollowing: Boolean,
    val isFollowed: Boolean,
    val status: UiStatus,
    val isAccessible: Boolean,
    val reported: Boolean = false
)
