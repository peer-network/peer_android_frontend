package eu.peernetwork.media.ui.renderer

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.annotation.Screen
import eu.peernetwork.media.ui.annotation.Timeline
import eu.peernetwork.media.ui.compose.AudioHost
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.compose.VolumeControl
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
        AudioPlayerThumbnail(
            path = path,
            position = position,
            hasControls = hasControls,
            isActive = isActive,
            enable = enable,
            length = length,
            current = current,
            session = session,
            source = { timelinePlayer }
        )
    }

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: AudioPlayer.Spec
    ) {
        val scope = rememberCoroutineScope()
        AudioHost(
            path = spec.path,
            position = spec.position,
            active = spec.isActive,
            enable = spec.enable,
            length = spec.length,
            progress = spec.progress,
            current = spec.current,
            session = session,
            source = { screenPlayer }
        ) { player, state, isLoading, isPlaying, mute, error ->
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
