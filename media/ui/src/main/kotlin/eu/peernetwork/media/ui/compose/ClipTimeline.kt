package eu.peernetwork.media.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun ClipTimeline(
    initialStart: Long,
    initialStop: Long,
    duration: Long,
    unit: Long,
    modifier: Modifier = Modifier,
    min: Long = 1,
    alignment: Alignment = Alignment.CenterStart,
    onTimeRangeChanged: (Long, Long) -> Unit = { _, _ -> },
    label: @Composable BoxScope.(Int, Long) -> Unit = { _, _  -> },
    spacer: Dp = 1.dp,
    mask: @Composable BoxScope.(Float, Float) -> Unit = { _, _ -> },
    background: @Composable BoxScope.(Int, Long) -> Unit = { _, _  -> },
    leading: @Composable BoxScope.(MutableState<Float>, State<Boolean>) -> Unit,
    trailing: @Composable (MutableState<Float>, State<Boolean>) -> Unit,
    track: @Composable BoxScope.(MutableState<Float>, MutableState<Float>, State<Pair<Boolean, Int>>) -> Unit,
    content: @Composable (Int) -> Unit,
) {
    val state = rememberLazyListState()
    val selectorState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val updatedLabel by rememberUpdatedState(label)
    val updatedLeading by rememberUpdatedState(leading)
    val updatedTrailing by rememberUpdatedState(trailing)
    val updatedTrack by rememberUpdatedState(track)
    val updatedMask by rememberUpdatedState(mask)
    val updatedContent by rememberUpdatedState(content)
    val updatedBackground by rememberUpdatedState(background)
    val updatedTimeRangeChanged by rememberUpdatedState(onTimeRangeChanged)
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scope.launch { state.scrollBy(-available.x) }
                return Offset.Zero
            }
        }
    }
    BoxWithConstraints(modifier = modifier) {
        val boxWidth = maxWidth
        val boxWidthPx by remember(boxWidth, density) {
            derivedStateOf { with(density) { boxWidth.toPx() } }
        }
        val spanCount = remember(duration, unit) { (duration / unit).toInt() }
        val totalItems = remember(duration, unit) { ((unit * 60) / duration).toInt() }
        val itemWidth = remember(maxWidth, totalItems) { maxWidth / totalItems }
        val itemWidthPx by remember(itemWidth, density) {
            derivedStateOf { with(density) { itemWidth.toPx() } }
        }
        val totalFrames = remember(totalItems) { totalItems * (duration / unit).toInt() }
        val startOffset = remember { mutableFloatStateOf(0f) }
        val stopOffset = remember { mutableFloatStateOf(0f) }
        val startPointer by remember(initialStart, boxWidthPx) {
            derivedStateOf {
                val itemIndex = initialStart / unit.toFloat()
                val itemOffset = itemIndex * boxWidthPx
                val scrollPx = with(selectorState) {
                    firstVisibleItemIndex * boxWidthPx + firstVisibleItemScrollOffset + startOffset.floatValue
                }
                val minOffset = (itemWidthPx * min)
                (itemOffset - scrollPx).coerceIn(-minOffset, boxWidthPx + minOffset)
            }
        }
        val currentStartOffset by remember(initialStart, boxWidthPx) {
            derivedStateOf {
                val itemIndex = initialStart / unit.toFloat()
                val itemOffset = itemIndex * boxWidthPx
                val scrollPx = with(selectorState) {
                    firstVisibleItemIndex * boxWidthPx + firstVisibleItemScrollOffset + startOffset.floatValue
                }
                (itemOffset - scrollPx).coerceIn(0f, boxWidthPx)
            }
        }
        val stopPointer by remember(initialStop, boxWidthPx) {
            derivedStateOf {
                val itemIndex = initialStop / unit.toFloat()
                val itemOffset = itemIndex * boxWidthPx
                val scrollPx = with(selectorState) {
                    firstVisibleItemIndex * boxWidthPx + firstVisibleItemScrollOffset + stopOffset.floatValue
                }
                val minOffset = (itemWidthPx * min)
                (itemOffset - scrollPx).coerceAtLeast(startPointer + minOffset)
                    .coerceAtMost(boxWidthPx)
            }
        }
        val actualStartTime by remember(startPointer, boxWidthPx, duration) {
            derivedStateOf {
                ((startPointer / (boxWidthPx * spanCount)) * duration).toLong()
            }
        }
        val actualStopTime by remember(stopPointer, boxWidthPx, duration) {
            derivedStateOf {
                ((stopPointer / (boxWidthPx * spanCount)) * duration).toLong()
            }
        }
        val selectedAreaWidth by remember(currentStartOffset, stopPointer) {
            derivedStateOf { (stopPointer - currentStartOffset).coerceAtLeast(0f) }
        }
        val isStartBoundReached = remember(startPointer) { derivedStateOf { startPointer <= 0f } }
        val isEndBoundReached = remember(stopPointer, boxWidthPx) { derivedStateOf { stopPointer >= boxWidthPx } }
        val visibleRange = remember(startPointer, stopPointer, boxWidthPx, isStartBoundReached, isEndBoundReached) {
            derivedStateOf {
                Pair(
                    isStartBoundReached.value && isEndBoundReached.value,
                    if (isStartBoundReached.value) {
                        1
                    } else if (isEndBoundReached.value) {
                        -1
                    } else {
                        0
                    }
                )
            }
        }
        val startIndex by remember(startPointer) {
            derivedStateOf { if (startPointer > 0) 1f else 0f }
        }
        val endOffsetPx by remember(stopPointer, boxWidthPx) {
            derivedStateOf { boxWidthPx - stopPointer }
        }
        LazyRow(
            state = state,
            userScrollEnabled = false,
            modifier = Modifier.align(alignment)
        ) {
            items(totalFrames, key = { it }) { index ->
                Row {
                    Spacer(modifier = Modifier.width(spacer))
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.width(itemWidth - spacer)
                    ) {
                        Box(modifier = Modifier.clipToBounds()) {
                            updatedContent(index)
                        }
                        updatedLabel(index, unit)
                    }
                }
            }
        }
        LazyRow(
            state = selectorState,
            modifier = Modifier.nestedScroll(nestedScrollConnection)
                .matchParentSize()
        ) {
            items(spanCount) { index ->
                Box(modifier = Modifier.width(boxWidth)) {
                    updatedBackground(index, unit)
                }
            }
        }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = 0f }
                .width(with(density) { startPointer.toDp() })
                .align(alignment)
        ) { updatedMask(startPointer, stopPointer) }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = stopPointer }
                .width(endOffsetPx.dp)
                .align(alignment)
        ) { updatedMask(stopPointer, endOffsetPx) }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = currentStartOffset }
                .width(with(density) { selectedAreaWidth.toDp() })
                .align(alignment)
        ) { updatedTrack(startOffset, stopOffset, visibleRange) }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = startPointer
                        .coerceAtMost(stopPointer - size.width)
                        .coerceAtLeast(0f)
                }.align(alignment)
                .zIndex(startIndex)
        ) { updatedLeading(startOffset, isStartBoundReached) }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = (stopPointer - size.width)
                        .coerceIn(0f, boxWidthPx)
                }.align(alignment)
        ) { updatedTrailing(stopOffset, isEndBoundReached) }
        LaunchedEffect(actualStartTime, actualStopTime) {
            snapshotFlow { actualStartTime to actualStopTime }
                .distinctUntilChanged()
                .collect { (start, stop) ->
                    updatedTimeRangeChanged(start, stop)
                }
        }
    }
}

@Composable
fun BoxScope.ClipTimelineLabel(
    position: Int,
    modifier: Modifier = Modifier
) {
    val isMajor = ((position + 1) % 2).toInt() == 0
    Column(modifier = Modifier.graphicsLayer {
        translationX = -(size.width / 2)
    }.then(modifier)) {
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
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewClipTimeline() {
    PeerTheme {
        ClipTimeline(
            12,
            15,
            60,
            10,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { position, unit -> ClipTimelineLabel(position, Modifier.height(140.dp)) },
            mask = { start, end ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Black.copy(alpha = .5f)))
            },
            track = { start, end, size ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White.copy(alpha = .05f)))
            },
            leading = { position, _ ->
                Box(modifier = Modifier
                    .width(16.dp)
                    .height(24.dp)
                    .background(Color.Red))
            },
            trailing = { position, _ ->
                Box(modifier = Modifier
                    .width(16.dp)
                    .height(24.dp)
                    .background(Color.Green))
            }
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.tertiary))
        }
    }
}
