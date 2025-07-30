package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun VideoRange(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    duration: Long,
    unit: Long,
    min: Long,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    ClipTimeline(
        initialStart = remember { start.value },
        initialStop = remember { stop.value },
        duration = duration,
        unit = unit,
        min = min,
        modifier = modifier,
        onTimeRangeChanged = { begin, end ->
            start.value = begin
            stop.value = end
        },
        label = { position, unit ->
            ClipTimelineLabel(position, modifier = Modifier.height(128.dp))
        },
        mask = { start, end ->
            Box(modifier = Modifier.fillMaxWidth()
                .height(64.dp)
                .background(Color.Black.copy(alpha = .75f))
                .align(Alignment.Center))
        },
        leading = { offset, limitReached ->
            Box(modifier = Modifier.width(16.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(4.dp))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (!limitReached.value || delta > 0) {
                            offset.value -= delta
                        }
                    }
                ).background(MaterialTheme.colorScheme.onBackground)) {
                Box(modifier = Modifier.padding(vertical = 6.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .align(Alignment.TopCenter))
            }
        },
        trailing = { offset, limitReached ->
            Box(modifier = Modifier.width(16.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(4.dp))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (!limitReached.value || delta < 0) {
                            offset.value -= delta
                        }
                    }
                ).background(MaterialTheme.colorScheme.onBackground)) {
                Box(modifier = Modifier.padding(vertical = 6.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .align(Alignment.BottomCenter))
            }
        },
        track = { start, end, range ->
            Box(modifier = Modifier.fillMaxWidth()
                .height(64.dp)
                .then(
                    if (!range.value.first) {
                        Modifier.draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                if (range.value.second == 0) {
                                    start.value -= delta
                                    end.value -= delta
                                } else if (range.value.second == 1 && delta < 0) {
                                    start.value -= delta
                                    end.value -= delta
                                } else if (range.value.second == -1 && delta > 0) {
                                    start.value -= delta
                                    end.value -= delta
                                }
                            }
                        )
                    } else {
                        Modifier
                    }
                ).background(MaterialTheme.colorScheme.background.copy(alpha = .6f)))
        },
        content = content
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVideoRange() {
    PeerTheme {
        val start = remember { mutableLongStateOf(1) }
        val stop = remember { mutableLongStateOf(3) }
        VideoRange(
            start,
            stop,
            60,
            5,
            2,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.tertiary))
        }
    }
}