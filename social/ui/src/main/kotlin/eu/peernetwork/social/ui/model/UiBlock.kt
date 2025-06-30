package eu.peernetwork.social.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiBlock(
    val userId: String,
    val username: String,
    val slug: Int,
    val image: String
)
