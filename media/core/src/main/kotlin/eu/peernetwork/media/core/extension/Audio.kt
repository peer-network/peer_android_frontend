package eu.peernetwork.media.core.extension

import android.net.Uri
import eu.peernetwork.media.core.model.UiFile

fun UiFile.getName(): String {
    return props.getString("name") ?: uri.lastPathSegment ?: "Unknown"
}

fun UiFile.getCover(): Uri? {
    return props.getParcelable("cover")
}
