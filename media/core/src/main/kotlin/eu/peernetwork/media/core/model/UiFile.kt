package eu.peernetwork.media.core.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class UiFile(val uri: Uri, val path: String = uri.path ?: uri.toString())
