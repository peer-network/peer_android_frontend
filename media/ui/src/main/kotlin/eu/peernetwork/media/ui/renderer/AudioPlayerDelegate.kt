package eu.peernetwork.media.ui.renderer

import android.media.MediaPlayer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.ui.compose.AudioScaffold
import eu.peernetwork.media.ui.compose.Progress
import eu.peernetwork.media.ui.core.MediaSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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
        length: MutableLongState,
        progress: MutableFloatState,
        current: MutableState<Int>,
        modifier: Modifier
    ) {
        val scope = rememberCoroutineScope()
        val state = remember { mutableLongStateOf(System.currentTimeMillis()) }
        val unMute = session.mute().collectAsStateWithLifecycle(enable.value)
        val isPlaying = remember { derivedStateOf { current.value == position && unMute.value } }
        val isLoading = remember { mutableStateOf(false) }
        val player = remember { session.audioPlayer().apply {
            isLooping = true
            setOnBufferingUpdateListener { _, percent -> isLoading.value = percent < 100 }
            setOnPreparedListener {
                it.start()
                progress.floatValue = 0f
                length.longValue = it.duration.coerceAtLeast(1).toLong()
            }
            setOnInfoListener { _, what, _ ->
                when (what) {
                    MediaPlayer.MEDIA_INFO_BUFFERING_START -> isLoading.value = true
                    MediaPlayer.MEDIA_INFO_BUFFERING_END -> isLoading.value = false
                }
                true
            }
            setOnErrorListener { _, _, _ ->
                isLoading.value = false
                true
            }
            setOnCompletionListener {
                it.seekTo(0)
                it.start()
            }
        } }
        AudioScaffold(
            isPlaying = isPlaying,
            isLoading = isLoading,
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
        ) {
            Progress(
                progress = progress,
                length = length,
                onUpdate = { player.seekTo(it.toInt()) },
                modifier = Modifier.fillMaxWidth()
                    .height(height = 3.dp)
                    .padding(start = 2.dp, end = 6.dp)
            )
        }
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
                        player.prepareAsync()
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
        LaunchedEffect(isPlaying.value, pause.value) {
            while (isPlaying.value && !pause.value) {
                withFrameMillis {
                    progress.floatValue = player.currentPosition.toFloat() / length.longValue
                }
                delay(16)
            }
            player.pause()
        }
        DisposableEffect(Unit) {
            onDispose {
                if (enable.value && current.value == position) {
                    player.reset()
                } else if (current.value == position) {
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
        TODO("Not yet implemented")
    }
}
