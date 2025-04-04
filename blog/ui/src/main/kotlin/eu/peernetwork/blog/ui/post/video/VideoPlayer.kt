package eu.peernetwork.social.ui.content.video

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay

@Composable
fun VideoPlayer(
    videoUri: Uri?,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    onVideoClick: () -> Unit = {},
    playerRef: MutableState<ExoPlayer?> = mutableStateOf(null),
    trimStartMs: Long = 0L,
    trimEndMs: Long = Long.MAX_VALUE,
    onPositionChanged: (Long) -> Unit = {},
    onDurationChanged: (Long) -> Unit = {},
    onTrimChanged: (Long, Long) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .build().also {
                it.repeatMode = Player.REPEAT_MODE_ONE
                playerRef.value = it
            }
    }

    LaunchedEffect(videoUri) {
        if (videoUri != null) {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
            exoPlayer.prepare()
            while (exoPlayer.duration <= 0) {
                delay(100)
            }
            onDurationChanged(exoPlayer.duration)
            exoPlayer.playWhenReady = isPlaying
        }
    }
    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
    }
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            val pos = exoPlayer.currentPosition
            onPositionChanged(pos)
            if (pos >= trimEndMs) {
                exoPlayer.seekTo(trimStartMs)
            }
        }
    }
    LaunchedEffect(trimStartMs, trimEndMs) {
        if (exoPlayer.currentPosition < trimStartMs || exoPlayer.currentPosition > trimEndMs) {
            exoPlayer.seekTo(trimStartMs)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    Box(
        modifier = modifier
            .background(Color.Black)
    ) {
        if (videoUri != null) {
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
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Video Unavailable", color = Color.White)
            }
        }
    }
}