package eu.peernetwork.media.ui.renderer

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.annotation.Screen
import eu.peernetwork.media.ui.annotation.Timeline
import eu.peernetwork.media.ui.compose.AudioHost
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.compose.MediaControl
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
    private val session: MediaInteractor,
    @Screen private val screenPlayer: MediaPlayer,
    @Timeline private val timelinePlayer: MediaPlayer,
) : AudioPlayer {
    @Composable
    @OptIn(FlowPreview::class)
    override fun Thumbnail(
        path: String,
        position: Int,
        hasControls: Boolean,
        isActive: State<Boolean>,
        enable: State<Boolean>,
        length: MutableLongState,
        current: MutableState<Int>,
        modifier: Modifier
    ) {
        Box(modifier = modifier) {
            AudioPlayerThumbnail(
                path = path,
                position = position,
                hasControls = hasControls,
                isActive = isActive,
                enabled = enable,
                length = length,
                current = current,
                session = session,
                source = { timelinePlayer }
            )
        }
    }

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: AudioPlayer.Spec
    ) {
        val scope = rememberCoroutineScope()
        val state = remember { mutableLongStateOf(System.currentTimeMillis()) }
        val repeat = remember { mutableStateOf(true) }
        val isPlaying = remember { mutableStateOf(false) }
        val isActive = remember(spec.enabled) { mutableStateOf(spec.enabled) }
        val status = remember(isPlaying.value) { mutableStateOf(isPlaying.value) }
        AudioHost(
            status = status,
            enabled = isActive,
            repeat = repeat,
            isPlaying = isPlaying,
            length = spec.length,
            progress = spec.progress,
            session = session,
            source = { screenPlayer },
        ) { player, isLoading, error ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                MediaControl(
                    isLoading = isLoading,
                    isPlaying = isPlaying,
                    error = error,
                    modifier = Modifier.fillMaxSize(),
                    onPlay = {
                        isActive.value = !isActive.value
                        if (isActive.value && error.value != null) {
                            state.longValue = System.currentTimeMillis()
                            status.value = true
                        } else if (isPlaying.value) {
                            player.pause()
                            status.value = false
                            isActive.value = false
                        } else {
                            player.start()
                            status.value = true
                            isActive.value = true
                        }
                    }
                )
            }
            LaunchedEffect(spec.enabled, state.longValue) {
                if (spec.enabled) {
                    player.reset()
                    status.value = true
                    isLoading.value = true
                    spec.current.value = spec.position
                    player.setDataSource(spec.path)
                    player.prepareAsync()
                    scope.launch { session.unmute(true) }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    if (spec.current.value == spec.position) {
                        player.pause()
                        repeat.value = false
                        isActive.value = false
                        status.value = false
                        isPlaying.value = false
                    }
                }
            }
        }
    }
}
