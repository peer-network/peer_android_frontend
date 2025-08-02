package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@Composable
fun Clips(
    start: Long,
    stop: Long,
    duration: Long,
    frameSize: Int,
    modifier: Modifier = Modifier,
    minFrameSize: Int = 2,
    spacer: Dp = 1.dp,
    state: LazyListState = rememberLazyListState(),
    contentAlignment: Alignment = Alignment.TopStart,
    onTimeRangeChanged: (Float, Float) -> Unit = { _, _ -> },
    mask: @Composable BoxScope.() -> Unit = {},
    leading: @Composable BoxScope.() -> Unit,
    trailing: @Composable BoxScope.() -> Unit,
    highlight: @Composable BoxScope.(State<Float>, State<Float>) -> Unit,
    content: @Composable (Int) -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val updateLeading by rememberUpdatedState(leading)
    val updateTrailing by rememberUpdatedState(trailing)
    val updateHighlight by rememberUpdatedState(highlight)
    val handleTimeRangeChanged by rememberUpdatedState(onTimeRangeChanged)
    val startOffset = remember { mutableFloatStateOf(0f) }
    val stopOffset = remember { mutableFloatStateOf(0f) }
    TimelineScaffold(
        start = start,
        stop = stop,
        duration = duration,
        frameSize = frameSize,
        startOffset = startOffset,
        stopOffset = stopOffset,
        modifier = modifier,
        spacer = spacer,
        state = state,
        mask = mask,
        contentAlignment = contentAlignment,
        item = content
    ) { startPointer, stopPointer ->
        val width = remember(maxWidth) { with(density) { maxWidth.toPx() } }
        val zIndex by remember(startPointer.value) {
            derivedStateOf { if (startPointer.value > 0) 1f else 0f }
        }
        val selectedAreaWidth by remember(startPointer.value, stopPointer.value) {
            derivedStateOf { (stopPointer.value - startPointer.value).coerceAtLeast(0f) }
        }
        val startLimit = remember(startPointer.value) {
            derivedStateOf { startPointer.value <= 0f }
        }
        val endLimit = remember(stopPointer, width) {
            derivedStateOf { stopPointer.value >= width }
        }
        val isDraggable = remember {
            derivedStateOf {
                state.layoutInfo.viewportStartOffset == 0 &&
                        !endLimit.value ||
                        frameSize == duration.toInt()
            }
        }
        val length = remember(duration, frameSize) { (duration / frameSize) * frameSize }
        val itemWidth = remember(width, frameSize) { (width / frameSize) * minFrameSize }
        val actualStartTime = remember(startOffset.floatValue, length) {
            derivedStateOf {
                (start - (startOffset.floatValue / (width * length)) * duration)
                    .coerceIn(0f, duration.toFloat())
            }
        }
        val actualStopTime = remember(stopOffset.floatValue, length) {
            derivedStateOf {
                (stop - (stopOffset.floatValue / (width * length)) * duration)
                    .coerceIn(0f, duration.toFloat())
            }
        }
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier.graphicsLayer { translationX = startPointer.value }
                .width(with(density) { selectedAreaWidth.toDp() })
                .then(
                    if (!(startLimit.value || endLimit.value) || isDraggable.value) {
                        Modifier.draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                if (!startLimit.value && delta < 0) {
                                    startOffset.floatValue -= delta
                                    stopOffset.floatValue -= delta
                                } else if (!endLimit.value && delta > 0) {
                                    startOffset.floatValue -= delta
                                    stopOffset.floatValue -= delta
                                } else {
                                    scope.launch { state.scrollBy(-delta) }
                                }
                            }
                        )
                    } else {
                        Modifier
                    }
                ).zIndex(9f)
        ) { updateHighlight(actualStartTime, actualStopTime) }
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier
                .graphicsLayer {
                    translationX = startPointer.value
                        .coerceAtMost(width - size.width)
                        .coerceAtLeast(0f)
                }.draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        startOffset.floatValue -= delta
                        val currentDistance = stopPointer.value - startPointer.value
                        if (currentDistance < itemWidth) {
                            startOffset.floatValue = startOffset.floatValue -
                                    (currentDistance % itemWidth) + itemWidth
                        }
                    }
                ).zIndex(zIndex)
        ) { updateLeading() }
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier
                .graphicsLayer {
                    translationX = (stopPointer.value - size.width)
                        .coerceAtLeast(0f)
                }.draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        stopOffset.floatValue -= delta
                        val currentDistance = stopPointer.value - startPointer.value
                        if (currentDistance < itemWidth) {
                            stopOffset.floatValue = stopOffset.floatValue +
                                    (currentDistance % itemWidth) - itemWidth
                        }
                    }
                )
        ) { updateTrailing() }
        LaunchedEffect(actualStartTime, actualStopTime) {
            handleTimeRangeChanged(
                actualStartTime.value,
                actualStopTime.value
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewClips() {
    PeerTheme {
        Clips(
            start = 1,
            stop = 4,
            duration = 5,
            frameSize = 5,
            mask = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Black.copy(alpha = .5f)))
            },
            leading = {
                Box(modifier = Modifier
                    .width(16.dp)
                    .height(24.dp)
                    .background(Color.Red))
            },
            trailing = {
                Box(modifier = Modifier
                    .width(16.dp)
                    .height(24.dp)
                    .background(Color.Green))
            },
            modifier = Modifier.fillMaxWidth(),
            highlight = { start, stop ->
                Box(modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = .1f))
                ) {
                    Text(
                        "${start.value}, ${stop.value}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        ) {
            Text("$it",modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.tertiary))
        }
    }
}
