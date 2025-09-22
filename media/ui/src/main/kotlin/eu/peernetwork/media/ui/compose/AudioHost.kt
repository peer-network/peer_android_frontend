package eu.peernetwork.media.ui.compose

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.ui.interactor.MediaInteractor
import eu.peernetwork.media.ui.exception.MediaPlaybackException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@Composable
fun AudioHost(
    status: State<Boolean>,
    enabled: State<Boolean>,
    repeat: State<Boolean> = enabled,
    isPlaying: MutableState<Boolean>,
    length: MutableLongState,
    progress: MutableFloatState,
    session: MediaInteractor,
    source: () -> MediaPlayer,
    content: @Composable (
        MediaPlayer,
        MutableState<Boolean>,
        State<Throwable?>
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val volume = session.volume().collectAsStateWithLifecycle(enabled.value)
    val isLoading = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val player = remember { source().apply {
        isLooping = true
        setOnPreparedListener {
            isLoading.value = false
            if (status.value) {
                it.start()
            }
            length.longValue = it.duration.coerceAtLeast(1).toLong()
        }
        setOnErrorListener { _, what, extra ->
            if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                isLoading.value = false
                errorState.value = MediaPlaybackException(what, extra)
            }
            true
        }
        setOnCompletionListener {
            it.seekTo(0)
            if (repeat.value) {
                it.start()
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    val observer = remember { LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if (isPlaying.value) {
                    player.pause()
                }
            }
            Lifecycle.Event.ON_RESUME -> {
                if (isPlaying.value) {
                    player.start()
                }
            }
            else -> Unit
        }
    } }
    updatedContent(
        player,
        isLoading,
        errorState
    )
    LaunchedEffect(volume.value) {
        scope.launch {
            val volume = if (volume.value) 1f else 0f
            player.setVolume(volume, volume)
        }
    }
    LaunchedEffect(enabled.value) {
        while (enabled.value) {
            withFrameMillis {
                isPlaying.value = player.isPlaying
                val currentProgress = player.currentPosition.toFloat() / length.longValue
                progress.floatValue = currentProgress
            }
            delay(16)
            isPlaying.value = player.isPlaying
        }
        isPlaying.value = false
        isLoading.value = false
        progress.floatValue = 0f
    }
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
