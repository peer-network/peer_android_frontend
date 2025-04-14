package eu.peernetwork.media.ui.renderer

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import eu.peernetwork.media.core.provider.VideoProvider
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.ui.provider.MediaPlayer
import javax.inject.Inject

class VideoPlayerDelegate @Inject constructor(
    provider: VideoProvider
) : VideoPlayer {
    private val interactor = provider.preview

    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: VideoPlayer.Spec
    ) {
        var isPlaying by remember { mutableStateOf(true) }
        val exoPlayer = remember {
            (interactor as MediaPlayer).player().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                setMediaItem(MediaItem.fromUri(spec.url))
                prepare()
            }
        }
        var progress by remember { mutableFloatStateOf(0f) }
        var totalDuration by remember { mutableLongStateOf(0L) }
        var isSeeking by remember { mutableStateOf(false) }

        LaunchedEffect(exoPlayer) {
            exoPlayer.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    totalDuration = player.duration.coerceAtLeast(0L)
                    progress = if (totalDuration > 0) {
                        player.currentPosition.toFloat() / totalDuration.toFloat()
                    } else 0f
                }
            })
        }

        LaunchedEffect(isPlaying) {
            exoPlayer.playWhenReady = isPlaying
        }

        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.stop()
            }
        }

        Box(
            modifier = modifier
                .background(Color.Black)
                .clickable { isPlaying = !isPlaying }
        ) {
            if (spec.url.isNotBlank()) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = false
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isPlaying = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isSeeking) 4.dp else 2.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(if (isSeeking) 4.dp else 2.dp)
                        .background(if (isSeeking) Color.White else Color.Gray)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.BottomCenter)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isSeeking = true },
                            onDragEnd = { isSeeking = false },
                            onHorizontalDrag = { change, _ ->
                                val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                                progress = newProgress
                                exoPlayer.seekTo((totalDuration * newProgress).toLong())
                            }
                        )
                    }
                    .background(Color.Transparent)
            )
        }
    }
}
