package eu.peernetwork.media.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import eu.peernetwork.media.core.renderer.MediaController
import eu.peernetwork.media.ui.compose.Progress
import eu.peernetwork.media.ui.interactor.MediaInteractor
import eu.peernetwork.media.ui.player.PlayerAudio
import eu.peernetwork.media.ui.player.PlayerVolume
import eu.peernetwork.media.ui.power.PowerScreen
import kotlinx.coroutines.delay
import javax.inject.Inject

class MediaControllerDelegate @Inject constructor(
    private val interactor: MediaInteractor
) : MediaController {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: MediaController.Spec
    ) {
        PowerScreen(
            path = spec.path,
            translucent = spec.translucent,
            interactor = interactor
        )
    }

    @Composable
    override fun Progress(
        progress: MutableFloatState,
        isPlaying: State<Boolean>,
        modifier: Modifier
    ) {
        val player = remember { interactor.exoPlayer() }
        val length = remember { mutableLongStateOf(0) }
        Progress(
            progress = progress,
            length = length,
            onUpdate = { offset, time ->
                if (isPlaying.value) {
                    progress.floatValue = offset
                    player.seekTo(time)
                }
            },
            modifier = modifier
        )
        LaunchedEffect(isPlaying.value) {
            while (isPlaying.value) {
                withFrameMillis {
                    length.longValue = player.duration.coerceAtLeast(1L)
                    progress.floatValue =
                        player.currentPosition.toFloat() / length.longValue
                }
                delay(16)
            }
        }
    }

    @Composable
    override fun Volume() {
        PlayerVolume(interactor)
    }

    @Composable
    override fun Content(
        path: String,
        expanded: Boolean,
        enabled: State<Boolean>,
        isPlaying: State<Boolean>,
        length: MutableLongState,
        modifier: Modifier,
        onToggle: (Boolean) -> Unit
    ) {
        PlayerAudio(
            path = path,
            expanded = expanded,
            enabled = enabled,
            isPlaying = isPlaying,
            length = length,
            modifier = modifier,
            interactor = interactor,
            onToggle = onToggle
        )
    }
}
