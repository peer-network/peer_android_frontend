package eu.peernetwork.media.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.MediaSelector
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.media.ui.selector.music.MusicCreate
import eu.peernetwork.media.ui.selector.photo.PhotoCreate
import eu.peernetwork.media.ui.selector.video.VideoCreate
import javax.inject.Inject

class MediaSelectorDelegate @Inject constructor() : MediaSelector {
    @Composable
    override fun invoke(modifier: Modifier, spec: MediaSelector.Spec) {
        when(spec.type) {
            MimeType.Music -> MusicCreate()
            MimeType.Video -> VideoCreate(spec.attachments) {}
            MimeType.Photo -> PhotoCreate(spec.attachments, onPhotosSelected = {})
            else -> {}
        }
    }
}
