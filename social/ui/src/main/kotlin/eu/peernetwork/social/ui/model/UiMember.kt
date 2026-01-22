package eu.peernetwork.social.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiMember(
    val id: String,
    val slug: String,
    val username: String,
    val imageUrl: String,
    val isFollowed: Boolean,
    val isFollowing: Boolean,
    val isAccessible: Boolean,
    val status: UiStatus
)
