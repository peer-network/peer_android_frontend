package eu.peernetwork.media.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.compose.AudioScaffold
import eu.peernetwork.media.ui.core.MediaSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioPlayerDelegate @Inject constructor(
    private val session: MediaSession
) : AudioPlayer {
    @Composable
    @OptIn(FlowPreview::class)
    override fun Thumbnail(
        path: String,
        position: Int,
        pause: State<Boolean>,
        enable: State<Boolean>,
        current: MutableState<Int>,
        modifier: Modifier
    ) {
        val scope = rememberCoroutineScope()
        val player = remember { session.audioPlayer() }
        val state = remember { mutableLongStateOf(System.currentTimeMillis()) }
        val unMute = session.mute().collectAsStateWithLifecycle(enable.value)
        val isPlaying = remember {
            derivedStateOf { current.value == position && unMute.value }
        }
        AudioScaffold(
            isPlaying = isPlaying,
            onPlayPauseClick = {
                if (current.value != position) {
                    current.value = position
                    scope.launch { session.mute(true) }
                    state.longValue = System.currentTimeMillis()
                } else if (isPlaying.value) {
                    player.pause()
                    current.value = -1
                    scope.launch { session.mute(false) }
                } else {
                    scope.launch { session.mute(true) }
                    state.longValue = System.currentTimeMillis()
                }
            }
        ) {  }
        LaunchedEffect(Unit) {
            val enableFlow = snapshotFlow { enable.value }.distinctUntilChanged()
            val muteFlow = snapshotFlow { unMute.value }.distinctUntilChanged()
            val currentFlow = snapshotFlow { current.value }.distinctUntilChanged()
            combine(enableFlow, muteFlow, currentFlow) { enabled, unMuted, position ->
                Triple(enabled, unMuted, position)
            }.distinctUntilChanged()
                .debounce(300)
                .collectLatest { result ->
                    val playing = result.third == position && result.first && result.second
                    if (playing) {
                        player.reset()
                        player.setDataSource(path)
                        player.setOnPreparedListener { it.start() }
                        player.prepareAsync()
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { state.longValue }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { result ->
                    if (!enable.value && current.value == position) {
                        player.reset()
                        player.setDataSource(path)
                        player.prepare()
                        player.start()
                    }
                }
        }
        LaunchedEffect(pause.value) {
            if (pause.value) {
                player.pause()
            } else if (current.value != -1 && unMute.value) {
                player.start()
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                if (enable.value && current.value == position) {
                    player.reset()
                }
            }
        }
    }

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: AudioPlayer.Spec
    ) {
        TODO("Not yet implemented")
    }
}
