package eu.peernetwork.media.ui.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.interactor.MediaInteractor
import eu.peernetwork.media.ui.player.PlayerProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
    private val imageView: ImageView,
    private val session: MediaInteractor,
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
        onToggle: (Boolean) -> Unit
    ) {
        val player = remember { session.exoPlayer() }
        val isLoading = remember { mutableStateOf(false) }
        val mute = session.volume().collectAsStateWithLifecycle(
            initialValue = session.exoPlayer().isDeviceMuted
        )
        val handlePlay by rememberUpdatedState {
            player.playWhenReady = true
            player.setMediaItem(
                MediaItem.Builder()
                    .setUri(path)
                    .setMediaId(path)
                    .build()
            )
            player.prepare()
        }
        DisposableEffect(path) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    if (playing) {
                        isLoading.value = false
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    if (player.currentMediaItem?.mediaId == path) {
                        handlePlay()
                    }
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
                onPlay = onToggle
            )
        }
        LaunchedEffect(Unit) {
            snapshotFlow { mute.value }
                .distinctUntilChanged()
                .collect { muted ->
                    session.exoPlayer().volume = if (muted) 1f else 0f
                    if (player.currentMediaItem?.mediaId == path) {
                        if (mute.value) {
                            player.play()
                        } else {
                            player.pause()
                        }
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { isPlaying.value }
                .distinctUntilChanged()
                .debounce(500)
                .collect { playing ->
                    isLoading.value = playing
                    if (playing) {
                        if (player.currentMediaItem?.mediaId != path) {
                            handlePlay()
                        }
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { enable.value }
                .distinctUntilChanged()
                .collect { enabled ->
                    if (!enabled && player.currentMediaItem?.mediaId == path) {
                        player.pause()
                    }
                }
        }
        DisposableEffect(Unit) {
            onDispose {
                if (player.currentMediaItem?.mediaId == path) {
                    player.pause()
                }
            }
        }
    }

    @Composable
    @OptIn(FlowPreview::class)
    override fun invoke(modifier: Modifier, spec: AudioPlayer.Spec) {
        val enable = remember { mutableStateOf(true) }
        PlayerProvider(
            path = spec.path,
            state = spec.enabled,
            enabled = enable,
            interactor = session,
        ) { player, isEnabled ->
            spec.cover?.let {
                imageView(
                    Modifier,
                    spec = ImageView.Spec(
                        url = it,
                        ratio = null,
                        contentScale = ContentScale.Crop,
                        blur = 500f,
                    )
                )
                imageView(
                    Modifier,
                    spec = ImageView.Spec(
                        url = it,
                        ratio = spec.ratio,
                        zoomable = true
                    )
                )
            }
        }
    }
}
