package eu.peernetwork.blog.ui.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import eu.peernetwork.media.core.model.UiMimeType

@Stable
data class UiDraft(
    val title: String,
    val description: String,
    val media: UiMimeType,
    val attachments: List<Uri>,
    val cover: Uri? = null
) {
    @Immutable
    data class Field(
        val title: String,
        val description: String,
    )
}
