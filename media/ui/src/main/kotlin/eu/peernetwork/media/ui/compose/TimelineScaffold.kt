package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun TimelineScaffold(
    start: Long,
    stop: Long,
    duration: Long,
    frameSize: Int,
    startOffset: State<Float>,
    stopOffset: State<Float>,
    modifier: Modifier = Modifier,
    spacer: Dp = 1.dp,
    state: LazyListState = rememberLazyListState(),
    contentAlignment: Alignment = Alignment.TopStart,
    mask: @Composable BoxScope.() -> Unit = {},
    item: @Composable (Int) -> Unit,
    content: @Composable BoxWithConstraintsScope.(State<Float>, State<Float>) -> Unit,
) {
    val density = LocalDensity.current
    val updatedMask by rememberUpdatedState(mask)
    val updatedContent by rememberUpdatedState(content)
    Timeline(
        duration = duration,
        frameSize = frameSize,
        modifier = modifier,
        spacer = spacer,
        contentAlignment = contentAlignment,
        state = state,
        item = item,
    ) {
        val width = maxWidth
        val widthPx by remember(width, density) {
            derivedStateOf { with(density) { width.toPx() } }
        }
        val itemWidthPx = remember(width, frameSize, density) {
            with(density) { width.toPx() / frameSize }
        }
        val startPointer = remember(start, width) {
            derivedStateOf {
                val itemIndex = start / frameSize.toFloat()
                val itemOffset = itemIndex * widthPx
                val scrollPx = with(state) {
                    firstVisibleItemIndex * itemWidthPx + firstVisibleItemScrollOffset + startOffset.value
                }
                (itemOffset - scrollPx).coerceIn(0f, widthPx)
            }
        }
        val stopPointer = remember(stop, width) {
            derivedStateOf {
                val itemIndex = stop / frameSize.toFloat()
                val itemOffset = itemIndex * widthPx
                val scrollPx = with(state) {
                    firstVisibleItemIndex * itemWidthPx + firstVisibleItemScrollOffset + stopOffset.value
                }
                (itemOffset - scrollPx).coerceIn(0f, widthPx)
            }
        }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = 0f }
                .width(with(density) { startPointer.value.toDp() })
        ) { updatedMask() }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = stopPointer.value }
                .width(with(density) { (widthPx - stopPointer.value).toDp() })
        ) { updatedMask() }
        updatedContent(startPointer, stopPointer)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTimelineScaffold() {
    PeerTheme {
        val start = remember { mutableFloatStateOf(0f) }
        val stop = remember { mutableFloatStateOf(0f) }
        TimelineScaffold(
            start = 4,
            stop = 5,
            duration = 50,
            frameSize = 5,
            startOffset = start,
            stopOffset = stop,
            mask = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Black.copy(alpha = .5f)))
            },
            modifier = Modifier.fillMaxWidth(),
            item = {
                Text("$it",modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.tertiary))
            }
        ) { _,_ -> }
    }
}
