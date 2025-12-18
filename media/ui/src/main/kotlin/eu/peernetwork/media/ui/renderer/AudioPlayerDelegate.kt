package eu.peernetwork.media.ui.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
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
        onPlay: (Boolean) -> Unit
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
                onPlay = onPlay
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
    override fun Volume() {
        val scope = rememberCoroutineScope()
        val mute = session.volume().collectAsStateWithLifecycle(session.exoPlayer().isDeviceMuted)
        IconButton(
            onClick = {
                scope.launch { session.unmute(!mute.value) }
            },
            modifier = Modifier.size(28.dp),
        ) {
            Icon(
                painter = painterResource(if (mute.value) {
                    R.drawable.ic_unmuted
                } else {
                    R.drawable.ic_muted
                }),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    @OptIn(FlowPreview::class)
    override fun invoke(
        modifier: Modifier,
        spec: AudioPlayer.Spec
    ) {
        val player = remember { session.exoPlayer() }
        LaunchedEffect(Unit) {
            snapshotFlow { spec.enabled.value }
                .distinctUntilChanged()
                .debounce(500)
                .collect { playing ->
                    if (playing) {
                        player.playWhenReady = true
                        player.setMediaItem(
                            MediaItem.Builder()
                                .setUri(spec.path)
                                .setMediaId(spec.path)
                                .build()
                        )
                        player.prepare()
                    }
                }
        }
        DisposableEffect(Unit) {
            onDispose {
                if (player.currentMediaItem?.mediaId == spec.path) {
                    player.pause()
                }
            }
        }
    }
}
