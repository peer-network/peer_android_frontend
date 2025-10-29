package eu.peernetwork.user.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiMetric(
    val posts: Int,
    val peers: Int,
    val followers: Int,
    val followed: Int,
)
