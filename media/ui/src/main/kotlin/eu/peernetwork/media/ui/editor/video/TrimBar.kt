package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset

@Composable
fun TrimBar(
    thumbs: List<Bitmap>,
    range: ClosedFloatingPointRange<Float>,
    max: Float,
    onRangeChanged: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    edgePadding: Dp = 28.dp
) {
    val ghostWidth = 24.dp
    val ghostColor = Color.Black.copy(alpha = 0.75f)
    val cornerRadius = 16.dp

    val thumbTouchPadding = 24.dp
    val minRange = 5_000f

    Box(
        modifier
            .padding(horizontal = edgePadding, vertical = 12.dp)
            .height(60.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .width(ghostWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius))
                    .background(ghostColor)
            )

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                userScrollEnabled = false
            ) {
                items(thumbs) { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .fillMaxHeight()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(ghostWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius))
                    .background(ghostColor)
            )
        }

        val clampColor = MaterialTheme.colorScheme.primary

        Canvas(Modifier.matchParentSize()) {

            val startPx = size.width * (range.start / max)
            val endPx = size.width * (range.endInclusive / max)
            val jawW = 24.dp.toPx()
            val radius = 16.dp.toPx()
            val outlineW = 4.dp.toPx()
            val height = size.height

            drawRoundRect(
                color = clampColor,
                topLeft = Offset(startPx, 0f),
                size = Size(endPx - startPx, height),
                cornerRadius = CornerRadius(radius),
                style = Stroke(width = outlineW)
            )

            val leftPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = startPx,
                        top = 0f,
                        right = startPx + jawW,
                        bottom = height,
                        topLeftCornerRadius = CornerRadius(radius, radius),
                        topRightCornerRadius = CornerRadius(0f, 0f),
                        bottomRightCornerRadius = CornerRadius(0f, 0f),
                        bottomLeftCornerRadius = CornerRadius(radius, radius)
                    )
                )
            }
            drawPath(leftPath, clampColor)

            val rightPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = endPx - jawW,
                        top = 0f,
                        right = endPx,
                        bottom = height,
                        topLeftCornerRadius = CornerRadius(0f, 0f),
                        topRightCornerRadius = CornerRadius(radius, radius),
                        bottomRightCornerRadius = CornerRadius(radius, radius),
                        bottomLeftCornerRadius = CornerRadius(0f, 0f)
                    )
                )
            }
            drawPath(rightPath, clampColor)
        }

        RangeSlider(
            value = range,
            valueRange = 0f..max,
            onValueChange = { newRange ->
                val clampedRange = if (newRange.endInclusive - newRange.start < minRange) {
                    val mid = (newRange.start + newRange.endInclusive) / 2
                    val halfMin = minRange / 2

                    val newStart = (mid - halfMin).coerceIn(0f, max - minRange)
                    val newEnd = newStart + minRange
                    newStart..newEnd
                } else {
                    newRange
                }

                onRangeChanged(clampedRange)
            },
            colors = SliderDefaults.colors(
                inactiveTrackColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                thumbColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
        )

        DraggableTrimOverlay(
            range = range,
            max = max,
            thumbPadding = thumbTouchPadding,
            onRangeChanged = onRangeChanged
        )

    }
}

@Composable
private fun DraggableTrimOverlay(
    range: ClosedFloatingPointRange<Float>,
    max: Float,
    thumbPadding: Dp,
    onRangeChanged: (ClosedFloatingPointRange<Float>) -> Unit
) {
    val currentRange by rememberUpdatedState(range)

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val sliderWidthPx = constraints.maxWidth.toFloat()
        val pxPerUnit = sliderWidthPx / max

        val startPx = range.start * pxPerUnit
        val endPx = range.endInclusive * pxPerUnit
        val thumbPaddingPx = with(LocalDensity.current) { thumbPadding.toPx() }

        val dragZoneStartPx = startPx + thumbPaddingPx
        val dragZoneEndPx = endPx - thumbPaddingPx
        val dragZoneWidth = (dragZoneEndPx - dragZoneStartPx).coerceAtLeast(1f)

        Box(
            modifier = Modifier
                .offset { IntOffset(dragZoneStartPx.toInt(), 0) }
                .width(with(LocalDensity.current) { dragZoneWidth.toDp() })
                .fillMaxHeight()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        down.consume()

                        val initialStart = currentRange.start
                        val initialEnd = currentRange.endInclusive
                        val rangeDuration = initialEnd - initialStart

                        var dragAmountX = 0f

                        drag(down.id) { change ->
                            val deltaX = change.positionChange().x
                            dragAmountX += deltaX
                            change.consume()

                            val deltaValue = dragAmountX / pxPerUnit
                            val newStart = (initialStart + deltaValue)
                                .coerceIn(0f, max - rangeDuration)
                            val newEnd = newStart + rangeDuration

                            onRangeChanged(newStart..newEnd)
                        }
                    }
                }
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
