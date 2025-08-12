package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Immutable
data class UiDraft(
    val title: String,
    val description: String,
    val media: UiMimeType,
    val attachment: UiAttachment,
) {
    @Immutable
    data class Field(
        val title: String,
        val description: String,
    )
}
