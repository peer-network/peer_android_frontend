package eu.peernetwork.media.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiMediaProperty(
    val size: String,
    val description: String? = null,
    val resolution: Pair<Int, Int>? = null
)
