package eu.peernetwork.blog.ui.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Immutable
data class UiDraft(
    val title: String,
    val description: String,
    val media: UiMimeType,
    val attachment: UiAttachment,
    val cover: Uri? = null
) {
    @Immutable
    data class Field(
        val title: String,
        val description: String,
    )
}
