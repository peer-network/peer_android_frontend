package eu.peernetwork.media.ui.player

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PlayerScreen(
    path: String,
    interactor: MediaInteractor,
    content: @Composable (ExoPlayer) -> Unit
) {
    val player = remember { interactor.exoPlayer() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (player.currentMediaItem?.mediaId == path) {
                        player.play()
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    if (player.currentMediaItem?.mediaId == path) {
                        player.pause()
                    }
                }
                else -> Unit
            }
        }
    }
    val updatedContent by rememberUpdatedState(content)
    val mute = interactor.volume().collectAsStateWithLifecycle(
        initialValue = player.isDeviceMuted
    )
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
    updatedContent(player)
    LaunchedEffect(Unit) {
        snapshotFlow { mute.value }
            .distinctUntilChanged()
            .collect { muted ->
                player.volume = if (muted) 1f else 0f
                if (player.currentMediaItem?.mediaId == path) {
                    if (mute.value) {
                        player.play()
                    } else {
                        player.pause()
                    }
                }
            }
    }
}

@Composable
fun PlayerScreen(
    path: String,
    interactor: MediaInteractor,
    error: @Composable (ExoPlayer, PlaybackException) -> Unit,
    content: @Composable (ExoPlayer) -> Unit
) {
    val updatedError by rememberUpdatedState(error)
    val updatedContent by rememberUpdatedState(content)
    PlayerScreen(
        path = path,
        interactor = interactor,
    ) { player ->
        val exception = remember { mutableStateOf<PlaybackException?>(null) }
        Crossfade(exception.value) { target ->
            if (target != null) {
                updatedError(player, target)
            } else {
                updatedContent(player)
            }
        }
        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    exception.value = null
                }

                override fun onPlayerError(error: PlaybackException) {
                    exception.value = error
                }
            }
            player.addListener(listener)
            onDispose { player.removeListener(listener) }
        }
    }
}
