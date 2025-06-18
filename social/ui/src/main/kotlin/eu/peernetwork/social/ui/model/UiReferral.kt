package eu.peernetwork.social.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiReferral (
    val id: String,
    val username: String,
    val slug: String,
    val img: String
)