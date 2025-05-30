package eu.peernetwork.social.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiMember(
    val id: String,
    val slug: String,
    val username: String,
    val imageUrl: String,
)
