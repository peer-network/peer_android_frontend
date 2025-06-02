package eu.peernetwork.media.core.model

import android.net.Uri

data class UiDirectory(
    val name: String,
    val path: String,
    val uri: Uri,
    val thumbnail: String,
)
