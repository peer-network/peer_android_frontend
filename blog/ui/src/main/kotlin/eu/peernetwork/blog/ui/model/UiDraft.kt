package eu.peernetwork.blog.ui.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import eu.peernetwork.media.core.model.UiAttachment

@Immutable
data class UiDraft(
    val title: String,
    val description: String,
    val attachment: UiAttachment
) {
    @Immutable
    data class Field(
        val title: String,
        val description: String,
    )
}
