package eu.peernetwork.media.ui.compose

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.ui.core.MediaSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun AudioHost(
    path: String,
    position: Int,
    pause: State<Boolean>,
    enable: State<Boolean>,
    length: MutableLongState,
    current: MutableState<Int>,
    session: MediaSession,
    content: @Composable (
        MediaPlayer,
        MutableLongState,
        State<Boolean>,
        State<Boolean>,
        State<Boolean>
    ) -> Unit
) {
    val state = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val unMute = session.mute().collectAsStateWithLifecycle(enable.value)
    val isPlaying = remember { derivedStateOf { current.value == position && unMute.value } }
    val isLoading = remember { mutableStateOf(false) }
    val player = remember { session.audioPlayer().apply {
        isLooping = true
        setOnBufferingUpdateListener { _, percent -> isLoading.value = percent < 100 }
        setOnPreparedListener {
            it.start()
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
    val updatedContent by rememberUpdatedState(content)
    updatedContent(player, state, isLoading, isPlaying, unMute)
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
        if (current.value == position) {
            if (pause.value) {
                player.pause()
            } else if (unMute.value) {
                player.start()
            }
        }
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
