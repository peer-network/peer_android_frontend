package eu.peernetwork.media.ui.renderer

import android.content.Context
import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.ui.compose.VideoControl
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.core.MediaPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoPlayerDelegate @Inject constructor(
    private val context: Context,
    private val interactor: VideoInteractor
) : VideoPlayer {
    private val media = (interactor as MediaPlayer)

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: VideoPlayer.Spec
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val player = remember { media.player() }
        var isReady by remember { mutableStateOf(false) }
        val isPlaying = remember { mutableStateOf(false) }
        val hasSession = remember { mutableStateOf(false) }
        val errorState = remember { mutableStateOf<Throwable?>(null) }
        val session = remember { mutableLongStateOf(System.currentTimeMillis()) }
        val isLoading = remember { mutableStateOf(!spec.enabled) }
        var mute = media.mute().collectAsStateWithLifecycle(player.isDeviceMuted)
        val dimension = media.observer.collectAsStateWithLifecycle()
        val listener = remember {
            object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying.value = playing
                    if (playing && isLoading.value) {
                        isLoading.value = false
                    }
                }

                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.containsAny(Player.EVENT_POSITION_DISCONTINUITY,
                            Player.EVENT_TIMELINE_CHANGED)) {
                        spec.length.longValue = player.duration.coerceAtLeast(1L)
                        spec.progress.floatValue = player.currentPosition.toFloat() / spec.length.longValue
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    errorState.value = error
                    hasSession.value = false
                }
            }
        }
        val texture = remember { TextureView(context) }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { texture },
                update = {
                    it.alpha = 0f
                    val dimen = dimension.value[spec.url] ?: spec.ratio
                    val width = context.resources.displayMetrics.widthPixels
                    if (spec.resolution != null) {
                        if (dimen >= 1) {
                            val width = spec.resolution!!.first
                            it.layoutParams = it.layoutParams.apply {
                                this.width = width
                                this.height = (width / dimen).toInt()
                            }
                        } else {
                            val height = spec.resolution!!.second
                            it.layoutParams = it.layoutParams.apply {
                                this.width = (height * dimen).toInt()
                                this.height = height
                            }
                        }
                    } else {
                        it.layoutParams = it.layoutParams.apply {
                            this.width = width
                            this.height = (width / dimen).toInt()
                        }
                    }
                    it.alpha = dimension.value[spec.url]?.let { 1f } ?: 0f
                    isReady = dimension.value[spec.url] != null
                },
                modifier = Modifier.wrapContentSize()
                    .clickable {
                        if (!isPlaying.value && !hasSession.value) {
                            session.longValue = System.currentTimeMillis()
                            hasSession.value = true
                        } else if (!isPlaying.value) {
                            player.play()
                        } else {
                            player.pause()
                        }
                    }
            )
            VideoControl(
                isLoading = isLoading,
                isPlaying = isPlaying,
                error = errorState
            ) {
                if (!isPlaying.value && !hasSession.value) {
                    session.longValue = System.currentTimeMillis()
                    hasSession.value = true
                } else if (!isPlaying.value) {
                    player.play()
                } else {
                    player.pause()
                }
            }
        }
        LaunchedEffect(spec.enabled, session.longValue) {
            if (spec.enabled) {
                isLoading.value = true
                errorState.value = null
                texture.surfaceTexture?.let {
                    interactor.attach(it, spec.url)
                    player.addListener(listener)
                }
            } else {
                isLoading.value = false
                isPlaying.value = false
                player.pause()
                player.removeListener(listener)
            }
        }
        LaunchedEffect(isPlaying.value, spec.enabled) {
            while (isPlaying.value && spec.enabled) {
                withFrameMillis {
                    spec.length.longValue = player.duration.coerceAtLeast(1L)
                    spec.progress.floatValue = player.currentPosition.toFloat() / spec.length.longValue
                }
                delay(16)
            }
        }
        LaunchedEffect(mute.value) {
            player.volume = if (mute.value) {
                1f
            } else {
                0f
            }
        }
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        player.play()
                    }
                    Lifecycle.Event.ON_STOP -> {
                        player.pause()
                    }
                    else -> Unit
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    @Composable
    override fun Controller(
        modifier: Modifier,
        progress: MutableFloatState,
        length: MutableLongState
    ) {
        val scope = rememberCoroutineScope()
        val player = remember { media.player() }
        var mute = media.mute().collectAsStateWithLifecycle(player.isDeviceMuted)
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
                    .height(height = 3.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { },
                            onDragEnd = { },
                            onHorizontalDrag = { change, _ ->
                                val position = (change.position.x / size.width).coerceIn(0f, 1f)
                                progress.floatValue = position
                                player.seekTo((length.longValue * position).toLong())
                            }
                        )
                    }
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = .3f))

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.floatValue)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }
            VolumeControl(mute) { scope.launch { interactor.mute(it) } }
        }
    }
}
