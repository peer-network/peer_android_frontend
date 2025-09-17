package eu.peernetwork.media.ui.compose

import android.media.MediaPlayer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.launch

@Composable
fun AudioPlayerThumbnail(
    path: String,
    position: Int,
    hasControls: Boolean,
    isActive: State<Boolean>,
    enable: State<Boolean>,
    length: MutableLongState,
    current: MutableState<Int>,
    session: MediaInteractor,
    source: () -> MediaPlayer
) {
    val scope = rememberCoroutineScope()
    val progress = remember { mutableFloatStateOf(0f) }
    val unMute = session.mute().collectAsStateWithLifecycle(enable.value)
    val isEnabled = remember {
        derivedStateOf {
            unMute.value && isActive.value
        }
    }
    AudioHost(
        path = path,
        position = position,
        active = isEnabled,
        enable = enable,
        length = length,
        progress = progress,
        current = current,
        session = session,
        source = source
    ) { player, state, isLoading, isPlaying, mute, error ->
        if (hasControls) {
            AudioScaffold(
                isPlaying = isPlaying,
                isEnabled = isEnabled,
                isLoading = isLoading,
                onPlayPauseClick = {
                    if (current.value != position) {
                        current.value = position
                        scope.launch { session.mute(true) }
                        state.longValue = System.currentTimeMillis()
                    } else if (isPlaying.value && mute.value) {
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
        } else {
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
