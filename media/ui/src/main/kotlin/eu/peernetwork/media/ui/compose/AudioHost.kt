package eu.peernetwork.media.ui.compose

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.ui.core.MediaSession
import eu.peernetwork.media.ui.exception.MediaPlaybackException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun AudioHost(
    path: String,
    position: Int,
    active: State<Boolean>,
    enable: State<Boolean>,
    length: MutableLongState,
    current: MutableState<Int>,
    session: MediaSession,
    content: @Composable (
        MediaPlayer,
        MutableLongState,
        State<Boolean>,
        MutableState<Boolean>,
        MutableFloatState,
        State<Boolean>,
        State<Throwable?>
    ) -> Unit
) {
    val state = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val unMute = session.mute().collectAsStateWithLifecycle(enable.value)
    val progress = remember { mutableFloatStateOf(0f) }
    val isLoading = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val shouldPlay = remember { derivedStateOf { current.value == position && unMute.value } }
    val player = remember { session.audioPlayer().apply {
        isLooping = true
        setOnPreparedListener {
            if (unMute.value) {
                it.start()
            }
            length.longValue = it.duration.coerceAtLeast(1).toLong()
        }
        setOnErrorListener { _, what, extra ->
            errorState.value = MediaPlaybackException(what, extra)
            true
        }
        setOnCompletionListener {
            it.seekTo(0)
            it.start()
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    updatedContent(
        player,
        state,
        isLoading,
        isPlaying,
        progress,
        unMute,
        errorState
    )
    LaunchedEffect(Unit) {
        val enableFlow = snapshotFlow { enable.value }.distinctUntilChanged()
        val currentFlow = snapshotFlow { current.value }.distinctUntilChanged()
        combine(enableFlow, currentFlow) { enabled, position ->
            Pair(enabled, position)
        }.distinctUntilChanged()
            .debounce(300)
            .collectLatest { result ->
                if (result.second == position && result.first) {
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
                    isLoading.value = true
                }
            }
    }
    LaunchedEffect(active.value) {
        if (current.value == position) {
            if (!active.value) {
                player.pause()
            } else if (unMute.value) {
                player.start()
            }
        }
    }
    LaunchedEffect(shouldPlay.value) {
        while (shouldPlay.value) {
            withFrameMillis {
                val currentProgress = player.currentPosition.toFloat() / length.longValue
                isLoading.value = currentProgress == progress.floatValue && active.value
                isPlaying.value = currentProgress != progress.floatValue || isLoading.value
                progress.floatValue = currentProgress
            }
            delay(16)
        }
        isPlaying.value = false
        isLoading.value = false
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
