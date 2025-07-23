package eu.peernetwork.media.core.model

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Immutable

@Immutable
data class UiFile(
    val uri: Uri,
    val path: String = uri.path ?: uri.toString(),
    val name: String = uri.lastPathSegment ?: "Unknown",
    val props: Bundle = Bundle()
)
