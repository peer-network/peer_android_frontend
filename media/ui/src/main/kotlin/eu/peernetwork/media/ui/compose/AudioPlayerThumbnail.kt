package eu.peernetwork.media.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@Composable
fun AudioPlayerThumbnail(
    hasControls: Boolean,
    enabled: State<Boolean>,
    isPlaying: State<Boolean>,
    isLoading: State<Boolean>,
    length: MutableLongState,
    session: MediaInteractor,
    source: () -> ExoPlayer,
    onPlay: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val progress = remember { mutableFloatStateOf(0f) }
    val volume = session.volume().collectAsStateWithLifecycle(enabled.value)
    val player = remember { source() }
    val playing = remember(isPlaying.value, volume.value) {
        mutableStateOf(isPlaying.value && volume.value)
    }
    val handlePlay by rememberUpdatedState(onPlay)
    if (hasControls) {
        AudioScaffold(
            isPlaying = playing,
            isEnabled = enabled,
            isLoading = isLoading,
            onPlayPauseClick = {
                if (playing.value) {
                    playing.value = false
                    player.pause()
                } else {
                    player.play()
                    handlePlay(true)
                    playing.value = true
                }
                scope.launch { session.unmute(isPlaying.value) }
            }
        ) {
            Progress(
                progress = progress,
                length = length,
                onUpdate = { offset, time ->
                    if (isPlaying.value) {
                        progress.floatValue = offset
                        player.seekTo(time)
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .height(height = 3.dp)
                    .padding(end = 6.dp)
            )
            LaunchedEffect(enabled.value) {
                if (!enabled.value) {
                    player.pause()
                }
            }
        }
    } else {
        VolumeControl(volume) {
            scope.launch { session.unmute(it) }
            if (it) {
                player.pause()
            } else {
                player.pause()
            }
        }
    }
    LaunchedEffect(isPlaying.value, enabled.value, volume.value) {
        while (isPlaying.value && enabled.value && volume.value) {
            withFrameMillis {
                length.longValue = player.duration.coerceAtLeast(1L)
                progress.floatValue =
                    player.currentPosition.toFloat() / length.longValue
            }
            delay(16)
        }
    }
}
