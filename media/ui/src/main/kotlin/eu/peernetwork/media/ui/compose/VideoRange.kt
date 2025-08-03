package eu.peernetwork.media.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@Composable
fun VideoRange(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    duration: Long,
    frameSize: Int,
    minFrameSize: Int,
    state: LazyListState,
    content: @Composable (Long) -> Unit,
) {
    VideoRange(
        start = start,
        stop = stop,
        duration = duration,
        frameSize = frameSize,
        minFrameSize = minFrameSize,
        state = state,
        content = content,
        modifier = Modifier
    )
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun VideoRange(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    duration: Long,
    frameSize: Int,
    minFrameSize: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    content: @Composable (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return if (scrollState.value < scrollState.maxValue) {
                    scope.launch { scrollState.scrollBy(-available.x) }
                    available
                } else {
                    Offset.Zero
                }
            }
        }
    }
    val updatedContent by rememberUpdatedState(content)
    val initialStart = remember { mutableLongStateOf(start.value) }
    val initialStop = remember { mutableLongStateOf(stop.value) }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = maxWidth
        Row(modifier = modifier.then(
            Modifier.horizontalScroll(scrollState)
                .nestedScroll(nestedScrollConnection)
        )) {
            Spacer(modifier = Modifier.width(24.dp))
            Clips(
                start = initialStart.longValue,
                stop = initialStop.longValue,
                state = state,
                duration = duration,
                frameSize = frameSize,
                minFrameSize = minFrameSize,
                contentAlignment = Alignment.BottomStart,
                onTimeRangeChanged = { startTime, stopTime ->
                    start.value = startTime.toLong()
                    stop.value = stopTime.toLong()
                },
                mask = {
                    Box(modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .height(64.dp)
                        .background(Color.Black.copy(alpha = .8f))
                        .align(Alignment.Center))
                },
                leading = {
                    Box(modifier = Modifier.width(20.dp)
                        .height(68.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.onBackground)) {
                        Box(modifier = Modifier.padding(vertical = 6.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .align(Alignment.TopCenter))
                    }
                },
                trailing = {
                    Box(modifier = Modifier.width(20.dp)
                        .height(68.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.onBackground)) {
                        Box(modifier = Modifier.padding(vertical = 6.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .align(Alignment.BottomCenter))
                    }
                },
                modifier = modifier.width(width),
                highlight = { _, _ ->
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(64.dp))
                }
            ) {
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    VideoRangeLabel(
                        position = it,
                        minFrameSize = minFrameSize,
                        modifier = Modifier.height(96.dp)
                    )
                    Box(modifier = Modifier.clipToBounds()) {
                        updatedContent(it.toLong())
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.VideoRangeLabel(
    position: Int,
    minFrameSize: Int,
    modifier: Modifier = Modifier
) {
    val isMajor = ((position + 1) % minFrameSize).toInt() == 0
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .size(if (isMajor) 4.dp else 2.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            if (isMajor) {
                Text(
                    text = "$position",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(2.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
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
            duration = 60,
            frameSize = 5,
            minFrameSize = 2,
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.tertiary))
        }
    }
}
