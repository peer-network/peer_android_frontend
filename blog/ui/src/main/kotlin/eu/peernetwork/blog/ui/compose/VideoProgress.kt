package eu.peernetwork.blog.ui.compose


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    val fraction = remember(position, durationMs) {
        (position / durationMs.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    }

    var sliderPos by remember { mutableStateOf(fraction) }
    val interaction = remember { MutableInteractionSource() }
    val isDragging  by interaction.collectIsDraggedAsState()

    LaunchedEffect(position, isDragging) {
        if (!isDragging) sliderPos = fraction
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(36.dp)
    ) {
        Timestamp(position)
        Spacer(Modifier.width(8.dp))
        CustomSlider(
            value = sliderPos,
            onValueChange = { sliderPos = it },
            onValueChangeFinished = { onSeek((sliderPos * durationMs).roundToLong()) },
            thumbRadius = 5.dp,
            trackHeight = 4.dp,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Timestamp(durationMs)
    }
}

@Composable
private fun Timestamp(ms: Long) {
    val totalSec = (ms / 1_000).toInt()
    val m = totalSec / 60
    val s = totalSec % 60
    Text(
        text = "%d:%02d".format(m, s),
        style = MaterialTheme.typography.labelSmall,
        color = Color.White
    )
}