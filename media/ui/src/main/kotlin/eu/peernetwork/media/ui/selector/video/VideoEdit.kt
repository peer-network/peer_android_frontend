package eu.peernetwork.media.ui.selector.video

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.ui.compose.VideoWidget
import kotlinx.coroutines.delay

@Composable
fun VideoEdit(
    videoUri: Uri?,
    onClearVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var videoDuration by remember { mutableStateOf(0L) }
    var trimStart by remember { mutableStateOf(0L) }
    var trimEnd by remember { mutableStateOf(0L) }
    var currentPosition by remember { mutableStateOf(0L) }
    val playerRef = remember { mutableStateOf<ExoPlayer?>(null) }
    var lastVideoUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(videoUri) {
        if (videoUri != lastVideoUri) {
            lastVideoUri = videoUri
            isPlaying = false
            videoDuration = 0L
            trimStart = 0L
            trimEnd = 0L
            currentPosition = 0L
            playerRef.value?.let { player ->
                player.stop()
                player.clearMediaItems()
                if (videoUri != null) {
                    player.setMediaItem(MediaItem.fromUri(videoUri))
                    player.prepare()
                    while (!player.isPlaying && player.playbackState != Player.STATE_READY) {
                        delay(100)
                    }
                    player.playWhenReady = true
                    isPlaying = true
                }
            }
        }
    }
    LaunchedEffect(isPlaying, trimStart, trimEnd) {
        if (isPlaying) {
            while (isPlaying) {
                delay(100)
                playerRef.value?.let { player ->
                    val pos = player.currentPosition
                    currentPosition = pos
                    if (pos >= trimEnd || pos < trimStart) {
                        player.seekTo(trimStart)
                    }
                }
            }
        }
    }
    LaunchedEffect(playerRef.value) {
        playerRef.value?.let { player ->
            while (player.duration <= 0) {
                delay(100)
            }
            videoDuration = player.duration
            trimEnd = player.duration
            player.repeatMode = Player.REPEAT_MODE_OFF
            isPlaying = true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        VideoWidget(
            videoUri = videoUri,
            isPlaying = isPlaying,
            onVideoClick = { isPlaying = !isPlaying },
            playerRef = playerRef,
            trimStartMs = trimStart,
            trimEndMs = trimEnd,
            onPositionChanged = { pos -> currentPosition = pos },
            onDurationChanged = { duration ->
                videoDuration = duration
                trimEnd = duration
            },
            onTrimChanged = { start, end ->
                trimStart = start
                trimEnd = end
                if (isPlaying) {
                    playerRef.value?.seekTo(trimStart)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (videoUri != null && videoDuration > 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .padding(16.dp)
            ) {
                RangeSlider(
                    value = trimStart.toFloat()..trimEnd.toFloat(),
                    onValueChange = { range ->
                        trimStart = range.start.toLong()
                        trimEnd = range.endInclusive.toLong()
                        playerRef.value?.seekTo(trimStart)
                    },
                    valueRange = 0f..videoDuration.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatTime(trimStart),
                        color = Color.White
                    )
                    Text(
                        formatTime(trimEnd),
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}