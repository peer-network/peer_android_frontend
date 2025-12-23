package eu.peernetwork.media.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
@OptIn(FlowPreview::class)
fun PlayerProvider(
    path: String,
    state: State<Boolean>,
    enabled: State<Boolean>,
    interactor: MediaInteractor,
    onLoad: (ExoPlayer, String) -> Unit = { player, path -> player
        player.playWhenReady = true
        player.setMediaItem(
            MediaItem.Builder()
                .setUri(path)
                .setMediaId(path)
                .build()
        )
        player.prepare()
    },
    content: @Composable (ExoPlayer, State<Boolean>) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val updatedContent by rememberUpdatedState(content)
    val handlePlay by rememberUpdatedState(onLoad)
    PlayerScreen(
        path = path,
        interactor = interactor,
    ) { player ->
        LaunchedEffect(Unit) {
            snapshotFlow { enabled.value }
                .distinctUntilChanged()
                .collect { enabled ->
                    if (!enabled && player.currentMediaItem?.mediaId == path) {
                        player.pause()
                    } else if (enabled && state.value) {
                        player.play()
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { state.value }
                .distinctUntilChanged()
                .debounce(500)
                .collect { playing ->
                    if (playing) {
                        isLoading.value = true
                        handlePlay(player, path)
                    }
                }
        }
        updatedContent(player, isLoading)
        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    if (playing) {
                        isLoading.value = false
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    if (player.currentMediaItem?.mediaId == path) {
                        handlePlay(player, path)
                    }
                }
            }
            player.addListener(listener)
            onDispose { player.removeListener(listener) }
        }
    }
}
