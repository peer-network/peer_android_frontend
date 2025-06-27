package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap

@Composable
fun TrimBar(
    thumbs: List<Bitmap>,
    range: ClosedFloatingPointRange<Float>,
    max: Float,
    onRangeChanged: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    edgePadding: Dp = 16.dp
) {
    Box(
        modifier
            .padding(horizontal = edgePadding, vertical = 12.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clipToBounds()
    ) {

        LazyRow(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(thumbs) { idx, bmp ->
                val frameTime = max * idx / (thumbs.lastIndex).coerceAtLeast(1)
                val inside = frameTime in range.start..range.endInclusive
                val scale = if (inside) 1.20f else 1f

                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .aspectRatio(9f / 16f)
                        .fillMaxHeight()
                )
            }
        }

        Canvas(Modifier.matchParentSize()) {
            val startPx = size.width * (range.start / max)
            val endPx = size.width * (range.endInclusive / max)

            drawRect(
                color = Color.Black.copy(alpha = 0.55f),
                size = Size(startPx, size.height)
            )
            drawRect(
                color = Color.Black.copy(alpha = 0.55f),
                topLeft = Offset(endPx, 0f),
                size = Size(size.width - endPx, size.height)
            )
        }

        RangeSlider(
            value = range,
            valueRange = 0f..max,
            onValueChange = onRangeChanged,
            colors = SliderDefaults.colors(
                inactiveTrackColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                thumbColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTrimBar() {
    val frames = remember {
        List(10) { index ->
            createBitmap(90, 160).apply {
                eraseColor(android.graphics.Color.rgb(50 * index, 120, 200))
            }
        }
    }
    TrimBar(
       thumbs = frames,
       range = 1_000f..8_000f,
       max = 10_000f,
       onRangeChanged = {},
       modifier = Modifier.fillMaxWidth()
    )
}
