package eu.peernetwork.media.core.model

import androidx.compose.runtime.Immutable
import eu.peernetwork.core.ui.R

@Immutable
sealed class MimeType(
    val id: Int,
    val label: Int? = null,
) {
    data object Text: MimeType(
        R.drawable.ic_photo,
        R.string.text_label,
    )
    data object Photo: MimeType(
        R.drawable.ic_photo,
        R.string.photo_label,
    )
    data object Video: MimeType(
        R.drawable.ic_video,
        R.string.video_label,
    )
    data object Music: MimeType(
        R.drawable.ic_music,
        R.string.music_label,
    )
    companion object {
        val TYPES = arrayOf(Photo, Video)
        fun get(index: Int): MimeType = TYPES[index]
    }
}
