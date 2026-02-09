package eu.peernetwork.ads.domain.model

data class Content(
    val id: String,
    val title: String,
    val description: String,
    val path: String,
    val isAccessible: Boolean,
    val status: Status,
)
