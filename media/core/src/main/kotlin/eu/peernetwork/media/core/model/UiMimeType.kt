package eu.peernetwork.media.core.model

import androidx.compose.runtime.Immutable
import eu.peernetwork.core.ui.R

@Immutable
sealed class UiMimeType(
    val id: Int,
    val label: Int? = null,
) {
    data object Text: UiMimeType(
        id = R.drawable.ic_image,
        label = R.string.text_label,
    )
    data object Photo: UiMimeType(
        id = R.drawable.ic_image,
        label = R.string.photo_label,
    )
    data object Video: UiMimeType(
        id = R.drawable.ic_video,
        label = R.string.video_label,
    )
    data object Music: UiMimeType(
        id = R.drawable.ic_music,
        label = R.string.music_label,
    )
    companion object {
        val TYPES = arrayOf(Photo, Video)
        fun get(index: Int): UiMimeType? = TYPES[index]
    }
}
