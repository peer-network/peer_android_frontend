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
    active: State<Boolean>,
    length: MutableLongState,
    progress: MutableFloatState,
    session: MediaInteractor,
    source: () -> MediaPlayer,
    content: @Composable (
        MediaPlayer,
        MutableState<Boolean>,
        MutableState<Boolean>,
        State<Throwable?>
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val volume = session.volume().collectAsStateWithLifecycle(active.value)
    val isLoading = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val player = remember { source().apply {
        isLooping = true
        setOnPreparedListener {
            it.start()
            length.longValue = it.duration.coerceAtLeast(1).toLong()
        }
        setOnErrorListener { _, what, extra ->
            if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                errorState.value = MediaPlaybackException(what, extra)
            }
            true
        }
        setOnCompletionListener {
            it.seekTo(0)
            it.start()
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
        isPlaying,
        errorState
    )
    LaunchedEffect(volume.value) {
        scope.launch {
            val volume = if (volume.value) 1f else 0f
            player.setVolume(volume, volume)
        }
    }
    LaunchedEffect(active.value) {
        while (active.value) {
            withFrameMillis {
                val currentProgress = player.currentPosition.toFloat() / length.longValue
                isPlaying.value = true
                isLoading.value = currentProgress == progress.floatValue
                progress.floatValue = currentProgress
            }
            delay(16)
        }
        isPlaying.value = false
        isLoading.value = false
        player.pause()
    }
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
