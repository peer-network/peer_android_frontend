package eu.peernetwork.media.core.renderer

import android.net.Uri
import androidx.compose.runtime.MutableState
import eu.peernetwork.media.core.model.MimeType

interface MediaSelector : Renderer.Stateful<MediaSelector.Spec> {
    data class Spec(
        val type: MimeType?,
        val attachments: MutableState<List<Uri>>
    )
}
