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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.annotation.Screen
import eu.peernetwork.media.ui.compose.AudioHost
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.compose.MediaControl
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
    private val session: MediaInteractor,
    @Screen private val screenPlayer: MediaPlayer,
) : AudioPlayer {
    @Composable
    @OptIn(FlowPreview::class)
    override fun Thumbnail(
        path: String,
        hasControls: Boolean,
        enable: State<Boolean>,
        isPlaying: State<Boolean>,
        length: MutableLongState,
        modifier: Modifier,
        onPlay: (Boolean) -> Unit
    ) {
        val player = remember { session.exoPlayer() }
        val isReady = remember { mutableStateOf(isPlaying.value) }
        val isLoading = remember { mutableStateOf(false) }
        val mute = session.volume().collectAsStateWithLifecycle(session.exoPlayer().isDeviceMuted)
        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    if (playing) {
                        isLoading.value = false
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    player.setMediaItem(MediaItem.fromUri(path))
                    player.prepare()
                }
            }
            player.addListener(listener)
            onDispose { player.removeListener(listener) }
        }
        Box(modifier = modifier) {
            AudioPlayerThumbnail(
                hasControls = hasControls,
                enabled = enable,
                isPlaying = isPlaying,
                isLoading = isLoading,
                length = length,
                session = session,
                source = { player },
                onPlay = onPlay
            )
        }
        LaunchedEffect(Unit) {
            snapshotFlow { isPlaying.value }
                .distinctUntilChanged()
                .debounce(500)
                .collect { playing ->
                    isReady.value = playing
                    player.playWhenReady = playing
                    if (playing) {
                        isLoading.value = mute.value
                        player.setMediaItem(MediaItem.fromUri(path))
                        player.prepare()
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { mute.value }
                .distinctUntilChanged()
                .collect { muted ->
                    session.exoPlayer().volume = if (muted) 1f else 0f
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { enable.value }
                .distinctUntilChanged()
                .collect { enabled ->
                    if (!enabled && isReady.value) {
                        player.pause()
                    }
                }
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
