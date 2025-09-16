package eu.peernetwork.media.ui.renderer

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.annotation.Screen
import eu.peernetwork.media.ui.annotation.Timeline
import eu.peernetwork.media.ui.compose.AudioHost
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.compose.VideoControl
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.FlowPreview
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
        val isPaused = remember(spec.isActive.value) {
            mutableStateOf(spec.isActive.value)
        }
        AudioHost(
            path = spec.path,
            position = spec.position,
            active = isPaused,
            enable = spec.enable,
            length = spec.length,
            progress = spec.progress,
            current = spec.current,
            session = session,
            source = { screenPlayer }
        ) { player, state, isLoading, isPlaying, mute, error ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                VideoControl(
                    isLoading = isLoading,
                    isPlaying = isPlaying,
                    error = error,
                    modifier = Modifier.fillMaxSize(),
                    onPlay = {
                        isPaused.value = !isPaused.value
                    }
                )
            }
        }
    }
}
