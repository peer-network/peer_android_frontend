package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.roundToLong

@Composable
fun VideoProgress(
    player: ExoPlayer,
    durationMs: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val position by produceState(0L, player) {
        value = player.currentPosition
        while (true) {
            awaitFrame()
            value = player.currentPosition
        }
    }

    val fraction = (position / durationMs.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    var sliderPos by remember { mutableStateOf(0f) }

    val interaction = remember { MutableInteractionSource() }
    val isDragging by interaction.collectIsDraggedAsState()

    LaunchedEffect(fraction, isDragging) {
        if (!isDragging) {
            sliderPos = fraction
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        CustomSlider(
            value = sliderPos,
            onValueChange = { sliderPos = it },
            onValueChangeFinished = {
                onSeek((sliderPos * durationMs).roundToLong())
            },
            thumbRadius = 5.dp,
            trackHeight = 4.dp,
            modifier = Modifier
                .fillMaxWidth(),
            interactionSource = interaction
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Timestamp(position)
            Timestamp(durationMs)
        }
    }
}

@Composable
private fun Timestamp(ms: Long) {
    val totalSec = (ms / 1000).toInt()
    val m = totalSec / 60
    val s = totalSec % 60
    Text(
        text = "%d:%02d".format(m, s),
        style = MaterialTheme.typography.labelSmall,
        color = Color.White
    )
}
