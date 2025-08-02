package eu.peernetwork.media.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlin.math.roundToInt

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun Timeline(
    duration: Long,
    frameSize: Int,
    modifier: Modifier = Modifier,
    spacer: Dp = 1.dp,
    state: LazyListState = rememberLazyListState(),
    contentAlignment: Alignment = Alignment.TopStart,
    item: @Composable (Int) -> Unit,
    overlay: @Composable BoxWithConstraintsScope.() -> Unit = {},
) {
    val density = LocalDensity.current
    val updatedItem by rememberUpdatedState(item)
    val updatedOverlay by rememberUpdatedState(overlay)
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        val width = maxWidth
        val length = remember(duration, frameSize) {
            (duration / frameSize.toFloat()).roundToInt() * frameSize
        }
        val itemWidth = remember(width, frameSize) {
            with(density) { (width.toPx() / frameSize.toFloat()).toDp() }
        }
        LazyRow(state = state) {
            items(length, key = { it }) { index ->
                val finalWidth = if (index == length || index == 0) {
                    itemWidth
                } else {
                    itemWidth - spacer
                }
                Row {
                    if (index != length && index != 0) {
                        Spacer(modifier = Modifier.width(spacer))
                    }
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.width(finalWidth)
                    ) { updatedItem(index) }
                }
            }
        }
        updatedOverlay()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTimeline() {
    PeerTheme {
        Timeline(
            duration = 9,
            frameSize = 3,
            modifier = Modifier.fillMaxWidth(),
            item = {
                Text("$it",modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.tertiary))
            }
        )
    }
}
