package eu.peernetwork.media.ui.compose

import android.media.MediaPlayer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@Composable
fun AudioPlayerThumbnail(
    path: String,
    position: Int,
    hasControls: Boolean,
    isActive: State<Boolean>,
    enabled: State<Boolean>,
    length: MutableLongState,
    current: MutableState<Int>,
    session: MediaInteractor,
    source: () -> MediaPlayer
) {
    val scope = rememberCoroutineScope()
    val progress = remember { mutableFloatStateOf(0f) }
    val hasFocus = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }
    val volume = session.volume().collectAsStateWithLifecycle(enabled.value)
    val status = remember(isPlaying.value) { mutableStateOf(isPlaying.value) }
    val isSuspended = remember { mutableStateOf(false) }
    val isEnabled = remember { derivedStateOf {
        (enabled.value || hasFocus.value) && current.value == position && volume.value
    } }
    AudioHost(
        enabled = isEnabled,
        repeat = isActive,
        isPlaying = isPlaying,
        length = length,
        progress = progress,
        session = session,
        source = source,
        status = isActive
    ) { player, isLoading, error ->
        val loading = remember { derivedStateOf { isLoading.value && volume.value } }
        if (hasControls) {
            AudioScaffold(
                isPlaying = isPlaying,
                isEnabled = status,
                isLoading = loading,
                onPlayPauseClick = {
                    if (current.value != position) {
                        player.reset()
                        hasFocus.value = true
                        status.value = true
                        current.value = position
                        scope.launch { session.unmute(true) }
                    } else if (hasFocus.value) {
                        player.pause()
                        hasFocus.value = false
                        status.value = false
                        isSuspended.value = true
                        scope.launch { session.unmute(false) }
                    } else {
                        hasFocus.value = true
                        status.value = true
                        player.start()
                        scope.launch { session.unmute(true) }
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
                        .padding(end = 6.dp)
                )
                LaunchedEffect(enabled.value) {
                    if (!enabled.value) {
                        player.pause()
                        hasFocus.value = false
                    }
                }
            }
        } else {
            VolumeControl(volume) {
                scope.launch { session.unmute(it) }
                if (it) {
                    hasFocus.value = true
                    player.start()
                } else {
                    hasFocus.value = false
                    player.pause()
                }
            }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { isEnabled.value }.distinctUntilChanged()
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { result ->
                    if (result && !isSuspended.value) {
                        player.reset()
                        hasFocus.value = true
                        status.value = true
                        isLoading.value = true
                        isSuspended.value = false
                        player.setDataSource(path)
                        player.prepareAsync()
                    } else if (isActive.value) {
                        player.reset()
                        hasFocus.value = false
                    }
                }
        }
        LaunchedEffect(isActive.value) {
            if (!isActive.value && current.value == position) {
                hasFocus.value = false
                player.pause()
            } else if (current.value == position) {
                hasFocus.value = true
                player.start()
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                if (current.value == position || isEnabled.value) {
                    hasFocus.value = false
                    player.pause()
                }
            }
        }
    }
}
