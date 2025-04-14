package eu.peernetwork.media.core.model

data class Property(
    val size: String,
    val description: String? = null,
    val resolution: Pair<Int, Int>? = null
)
