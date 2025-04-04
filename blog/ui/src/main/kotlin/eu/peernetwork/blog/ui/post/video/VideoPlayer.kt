package eu.peernetwork.blog.ui.post.video

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUri: Uri?,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    onVideoClick: () -> Unit = {},
    playerRef: MutableState<ExoPlayer?> = mutableStateOf(null)
) {
    val context = LocalContext.current
    val currentUri by rememberUpdatedState(videoUri)
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .build().apply {
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
            }.also {
                playerRef.value = it
            }
    }
    LaunchedEffect(currentUri) {
        currentUri?.let { uri ->
            exoPlayer.setMediaItem(MediaItem.fromUri(uri))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = isPlaying
        }
    }
    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            playerRef.value = null
        }
    }
    Box(
        modifier = modifier
            .background(Color.Black)
    ) {
        if (currentUri != null) {
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