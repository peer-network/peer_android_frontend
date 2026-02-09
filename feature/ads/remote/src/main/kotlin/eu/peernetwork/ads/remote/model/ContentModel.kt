package eu.peernetwork.ads.remote.model

import eu.peernetwork.ads.domain.model.Status

data class ContentModel(
    val id: String,
    val title: String,
    val description: String,
    val path: String,
    val isAccessible: Boolean,
    val status: Status,
)
