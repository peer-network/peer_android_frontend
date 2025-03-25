package eu.peernetwork.user.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiPoint(
    val name: String,
    val used: Int,
    val available: Int
)
