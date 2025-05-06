package eu.peernetwork.social.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiPost(
    val id: String,
    val title: String,
    val description: String,
    val author: UiMember
)
