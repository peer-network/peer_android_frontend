package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun VideoContent(
    url: String,
    videoPlayer: VideoPlayer,
    player: ExoPlayer,
    durationMs: Long,
    trimStart: Long,
    trimEnd: Long,
    frames: List<Bitmap>,
    progressPct: Float?,
    onTrimChange: (Long, Long) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    var range by remember(trimStart, trimEnd) {
        mutableStateOf(trimStart.toFloat()..trimEnd.toFloat())
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val maxVideoHeight = screenHeight * 0.65f

    val isExporting = progressPct != null

    val currentStart by rememberUpdatedState(trimStart)
    val currentEnd by rememberUpdatedState(trimEnd)

    val listener = remember(currentStart, currentEnd) {
        object : Player.Listener {
            override fun onEvents(p: Player, events: Player.Events) {
                if (p.isPlaying &&
                    (p.currentPosition < currentStart || p.currentPosition >= currentEnd)
                ) {
                    p.seekTo(currentStart)
                }
            }
        }
    }

    LaunchedEffect(trimStart, trimEnd) {
        player.seekTo(trimStart)
    }

    DisposableEffect(player) {
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .heightIn(max = maxVideoHeight),
            contentAlignment = Alignment.Center
        ) {
            VideoPlayer(
                url = url,
                playing = true,
                videoPlayer = videoPlayer,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (frames.isNotEmpty()) {
            TrimBar(
                thumbs = frames,
                range = range,
                max = durationMs.toFloat(),
                onRangeChanged = {
                    range = it
                    onTrimChange(it.start.toLong(), it.endInclusive.toLong())
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (!isExporting) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DesignOutlinedButton (
                    onClick = onCancel,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 24.dp
                    ),
                    modifier = Modifier.height(38.dp),

                    ) {
                    Text(
                        stringResource(eu.peernetwork.core.ui.R.string.back_label),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                DesignButton(
                    onClick = onConfirm,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 24.dp
                    ),
                    modifier = Modifier.height(38.dp),

                    ) {
                    Text(
                        stringResource(eu.peernetwork.core.ui.R.string.confirm_trim),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        } else {
            LinearProgressIndicator(
                progress = { progressPct ?: 0f },
                color = MaterialTheme.colorScheme.onSurface,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 30.dp)
                    .height(8.dp)
            )
        }
    }
}

