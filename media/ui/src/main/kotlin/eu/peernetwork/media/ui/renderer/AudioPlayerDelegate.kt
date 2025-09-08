package eu.peernetwork.media.ui.renderer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.compose.AudioHost
import eu.peernetwork.media.ui.compose.AudioScaffold
import eu.peernetwork.media.ui.compose.Progress
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.core.MediaSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
    private val session: MediaSession
) : AudioPlayer {
    @Composable
    @OptIn(FlowPreview::class)
    override fun Thumbnail(
        path: String,
        position: Int,
        isActive: State<Boolean>,
        enable: State<Boolean>,
        length: MutableLongState,
        current: MutableState<Int>,
        modifier: Modifier
    ) {
        val scope = rememberCoroutineScope()
        AudioHost(
            path = path,
            position = position,
            active = isActive,
            enable = enable,
            length = length,
            current = current,
            session = session
        ) { player, state, isLoading, isPlaying, progress, mute, error ->
            AudioScaffold(
                isPlaying = isPlaying,
                isLoading = isLoading,
                onPlayPauseClick = {
                    if (current.value != position) {
                        current.value = position
                        scope.launch { session.mute(true) }
                        state.longValue = System.currentTimeMillis()
                    } else if (isPlaying.value) {
                        player.pause()
                        scope.launch { session.mute(false) }
                    } else {
                        player.start()
                        scope.launch { session.mute(true) }
                    }
                }
            ) {
                Progress(
                    progress = progress,
                    length = length,
                    onUpdate = { offset, time ->
                        progress.floatValue = offset
                        if (position == current.value) {
                            player.seekTo(time.toInt())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(height = 3.dp)
                        .padding(start = 2.dp, end = 6.dp)
                )
            }
        }
    }

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: AudioPlayer.Spec
    ) {
        val scope = rememberCoroutineScope()
        AudioHost(
            path = spec.path,
            position = spec.position,
            active = spec.isActive,
            enable = spec.enable,
            length = spec.length,
            current = spec.current,
            session = session
        ) { player, state, isLoading, isPlaying, progress, mute, error ->
            VolumeControl(mute) {
                scope.launch { session.mute(it) }
                if (it) {
                    player.start()
                } else {
                    player.pause()
                }
            }
        }
    }
}
