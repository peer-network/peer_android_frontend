package eu.peernetwork.media.ui.renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.ui.compose.VideoControl
import eu.peernetwork.media.ui.core.MediaPlayer
import eu.peernetwork.media.ui.view.TextureViewWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoPlayerDelegate @Inject constructor(
    private val context: Context,
    private val interactor: VideoInteractor
) : VideoPlayer {
    private val media = (interactor as MediaPlayer)

    private val widthPixels: Int get() = context.resources.displayMetrics.widthPixels

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: VideoPlayer.Spec
    ) {
        val player = remember { media.player() }
        val lifecycleOwner = LocalLifecycleOwner.current
        var session by remember { mutableLongStateOf(System.currentTimeMillis()) }
        var isReady by remember { mutableStateOf(false) }
        val isPlaying = remember(session) { mutableStateOf(player.isPlaying) }
        val isLoading = remember {
            derivedStateOf {
                !player.isPlaying && !isReady
            }
        }
        var mute = media.mute().collectAsStateWithLifecycle(player.isDeviceMuted)
        val dimension = media.observer.collectAsStateWithLifecycle()
        var progress = remember { mutableFloatStateOf(0f) }
        val isProcessing = remember { mutableStateOf(player.isLoading) }
        var totalDuration by remember { mutableLongStateOf(0L) }
        val listener = remember {
            object : Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    isProcessing.value = isLoading && player.isPlaying
                }
                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.containsAny(Player.EVENT_POSITION_DISCONTINUITY,
                            Player.EVENT_TIMELINE_CHANGED)) {
                        totalDuration = player.duration.coerceAtLeast(1L)
                        progress.floatValue = player.currentPosition.toFloat() / totalDuration
                    }
                }
            }
        }
        val scope = rememberCoroutineScope()
        val observer = remember {
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    player.play()
                    player.addListener(listener)
                } else if (event == Lifecycle.Event.ON_STOP && player.isPlaying) {
                    player.pause()
                    player.removeListener(listener)
                }
            }
        }
        val texture = remember {
            TextureViewWrapper(TextureView(context)).apply {
                attachCallback(
                    object : TextureView.SurfaceTextureListener {
                        override fun onSurfaceTextureAvailable(
                            texture: SurfaceTexture,
                            p1: Int,
                            p2: Int
                        ) { interactor.attach(texture, spec.url) }

                        override fun onSurfaceTextureSizeChanged(
                            p0: SurfaceTexture,
                            p1: Int,
                            p2: Int
                        ) {}

                        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
                            interactor.detach(texture)
                            return true
                        }

                        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
                    }
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.clickable {
                if (!player.isPlaying) {
                    player.play()
                } else {
                    player.pause()
                }
                session = System.currentTimeMillis()
            }
        ) {
            AndroidView(
                factory = { texture.view },
                update = {
                    it.alpha = 0f
                    val dimen = dimension.value[spec.url] ?: spec.ratio
                    it.layoutParams = it.layoutParams.apply {
                        this.width = widthPixels
                        this.height = (width / dimen).toInt()
                    }
                    it.alpha = dimension.value[spec.url]?.let { 1f } ?: 0f
                    isReady = dimension.value[spec.url]?.let { true } == player.isPlaying
                    session = System.currentTimeMillis()
                },
                modifier = modifier.wrapContentSize()
            )
            VideoControl(
                isLoading = isLoading,
                isPlaying = isPlaying,
                isProcessing = isProcessing,
                mute = mute,
                progress = progress,
                onMute = { scope.launch { interactor.mute(it) } },
                onUpdate = {
                    progress.floatValue = it
                    player.seekTo((totalDuration * it).toLong())
                }
            ) {
                if (!player.isPlaying) {
                    player.play()
                } else {
                    player.pause()
                }
                session = System.currentTimeMillis()
            }
        }
        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycle.addObserver(observer)
        }
        DisposableEffect(Unit) {
            onDispose {
                player.removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(player) {
            while (spec.enabled) {
                withFrameMillis {
                    totalDuration = player.duration.coerceAtLeast(1L)
                    progress.floatValue = player.currentPosition.toFloat() / totalDuration
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
    }
}
