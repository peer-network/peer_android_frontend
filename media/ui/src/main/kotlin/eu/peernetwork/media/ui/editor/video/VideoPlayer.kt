package eu.peernetwork.media.ui.editor.video

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun VideoPlayer(
    url: String,
    playing: Boolean,
    videoPlayer: VideoPlayer,
    modifier: Modifier = Modifier,
    ratio: Float = 1.77f
) {
    val spec = remember(url, ratio, playing) {
        VideoPlayer.Spec(
            url = url,
            ratio = ratio,
            enabled = playing,
            volume = 1f
        )
    }
    videoPlayer(modifier = modifier, spec = spec)
}

