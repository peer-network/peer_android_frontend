package eu.peernetwork.app.ui.profile.preview

import androidx.compose.runtime.Immutable
import eu.peernetwork.social.ui.R

@Immutable
sealed class ProfileMedia(
    val id: Int,
    val label: Int? = null,
) {
    data object Photo: ProfileMedia(
        R.drawable.ic_photo,
        R.string.photo_label,
    )
    data object Video: ProfileMedia(
        R.drawable.ic_video,
        R.string.video_label,
    )
    data object Music: ProfileMedia(
        R.drawable.ic_music,
        R.string.music_label,
    )
    companion object {
        val ROUTES = arrayOf(Photo, Video, Music)
        fun get(index: Int): ProfileMedia = ROUTES[index]
    }
}
