package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    thumbRadius: Dp = 5.dp,
    trackHeight: Dp = 4.dp,
    activeTrackColor: Color = Color.White,
    inactiveTrackColor: Color = Color.White.copy(alpha = .3f),
) {
    var isDragging by remember { mutableStateOf(false) }

    var sliderWidthPx by remember { mutableStateOf(0f) }

    Box(
        modifier
            .height(maxOf(thumbRadius * 2, trackHeight))
            .onSizeChanged { sliderWidthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onValueChangeFinished()
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = (value * sliderWidthPx) + dragAmount
                        onValueChange((newOffset / sliderWidthPx).coerceIn(0f, 1f))
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cy = center.y
            val trackPx = trackHeight.toPx()
            drawRoundRect(
                color = inactiveTrackColor,
                topLeft = Offset(0f, cy - trackPx / 2),
                size = Size(size.width, trackPx),
                cornerRadius = CornerRadius(trackPx / 2, trackPx / 2)
            )
            drawRoundRect(
                color = activeTrackColor,
                topLeft = Offset(0f, cy - trackPx / 2),
                size = Size(value * size.width, trackPx),
                cornerRadius = CornerRadius(trackPx / 2, trackPx / 2)
            )
        }

        val thumbPx = with(LocalDensity.current) { thumbRadius.toPx() }
        Box(
            Modifier
                .offset {
                    IntOffset(
                        x = ((value * sliderWidthPx) - thumbPx).roundToInt(),
                        y = 0
                    )
                }
                .size(thumbRadius * 2)
                .background(Color.White, shape = CircleShape)
                .align(Alignment.CenterStart)
        )
    }
}
