package eu.peernetwork.media.ui.power

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.ui.interactor.MediaInteractor
import eu.peernetwork.media.ui.player.PlayerScreen

@Composable
fun PowerScreen(
    path: String,
    translucent: Boolean = false,
    interactor: MediaInteractor,
) {
    val handlePlay by rememberUpdatedState { player: ExoPlayer, play: Boolean ->
        if (player.currentMediaItem?.mediaId == path) {
            if (play) {
                player.pause()
            } else {
                player.play()
            }
        } else {
            player.playWhenReady = true
            player.setMediaItem(
                MediaItem.Builder()
                    .setUri(path)
                    .setMediaId(path)
                    .build()
            )
            player.prepare()
        }
    }
    PlayerScreen(
        path = path,
        interactor = interactor,
        error = { player, exception ->
            PowerError(translucent) {
                handlePlay(player, false)
            }
        }
    ) { player ->
        val isPlaying = remember { mutableStateOf(player.isPlaying) }
        PowerButton(
            state = isPlaying,
            translucent = translucent
        ) { handlePlay(player, isPlaying.value) }
        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying.value = playing
                }
            }
            player.addListener(listener)
            onDispose { player.removeListener(listener) }
        }
    }
}
