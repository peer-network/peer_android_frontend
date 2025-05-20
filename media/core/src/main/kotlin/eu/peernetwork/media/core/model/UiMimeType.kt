package eu.peernetwork.media.core.model

import androidx.compose.runtime.Immutable
import eu.peernetwork.core.ui.R

@Immutable
sealed class UiMimeType(
    val id: Int,
    val label: Int? = null,
) {
    data object Text: UiMimeType(
        R.drawable.ic_photo,
        R.string.text_label,
    )
    data object Photo: UiMimeType(
        R.drawable.ic_photo,
        R.string.photo_label,
    )
    data object Video: UiMimeType(
        R.drawable.ic_video,
        R.string.video_label,
    )
    data object Music: UiMimeType(
        R.drawable.ic_music,
        R.string.music_label,
    )
    companion object {
        val TYPES = arrayOf(Photo, Video)
        fun get(index: Int): UiMimeType? = TYPES[index]
    }
}
