package eu.peernetwork.media.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(2.dp),
    color: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    progressColor: Color = MaterialTheme.colorScheme.onBackground,
    progress: MutableFloatState,
    length: MutableLongState,
    onUpdate: (Long) -> Unit
) {
    val handleOnUpdate by rememberUpdatedState(onUpdate)
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { },
                    onDragEnd = { },
                    onHorizontalDrag = { change, _ ->
                        val position = (change.position.x / size.width).coerceIn(0f, 1f)
                        progress.floatValue = position
                        handleOnUpdate((length.longValue * position).toLong())
                    }
                )
            }.clip(shape = shape)
            .background(color = color)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.floatValue)
                .fillMaxHeight()
                .clip(shape = shape)
                .background(progressColor)
        )
    }
}

@Preview
@Composable
fun ProgressPreview() {
    PeerTheme {
        val progress = remember { mutableFloatStateOf(0.5f) }
        val length = remember { mutableLongStateOf(10000L) }
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Progress(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                progress = progress,
                length = length
            ) {}
        }
    }
}
