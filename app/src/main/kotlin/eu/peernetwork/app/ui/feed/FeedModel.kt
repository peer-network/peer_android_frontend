package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Immutable
import eu.peernetwork.social.ui.R

@Immutable
sealed class FeedMedia(
    val id: Int,
    val label: Int? = null,
) {
    data object Photo: FeedMedia(
        R.drawable.ic_photo,
        R.string.photo_label,
    )
    data object Video: FeedMedia(
        R.drawable.ic_video,
        R.string.video_label,
    )
    data object Music: FeedMedia(
        R.drawable.ic_music,
        R.string.music_label,
    )
    companion object {
        val ROUTES = arrayOf(Photo, Video, Music)
        fun get(index: Int): FeedMedia = ROUTES[index]
    }
}
