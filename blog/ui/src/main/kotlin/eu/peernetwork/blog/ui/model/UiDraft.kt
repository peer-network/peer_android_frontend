package eu.peernetwork.blog.ui.model

import android.net.Uri
import eu.peernetwork.media.core.model.MimeType

data class UiDraft(
    val title: String,
    val description: String,
    val media: MimeType,
    val attachments: List<Uri>
)
