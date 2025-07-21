package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostIcon(
    action: UiAction,
    value: String,
    size: Dp = 28.dp,
    padding: PaddingValues = PaddingValues(4.dp),
    orientation: Orientation = Orientation.Horizontal,
    color: Color = MaterialTheme.colorScheme.tertiary,
    onClick: (UiAction) -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val alpha = if (pressed) 0.5f else 1f
    val clickHandler = remember(action) { { onClick(action) } }
    Box(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                pressed = true
                try {
                    awaitRelease()
                } finally {
                    pressed = false
                }
            },
            onTap = { clickHandler() }
        )
    }) {
        PostIcon(
            action = action,
            value = value,
            modifier = Modifier.padding(padding)
                .graphicsLayer { this.alpha = alpha },
            size = size,
            padding = padding,
            orientation = orientation,
            color = color,
        )
    }
}

@Composable
fun PostIcon(
    action: UiAction,
    value: String,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    padding: PaddingValues = PaddingValues(4.dp),
    orientation: Orientation = Orientation.Horizontal,
    color: Color = MaterialTheme.colorScheme.tertiary,
) {
    Box(modifier = modifier.padding(padding)) {
        if (orientation == Orientation.Horizontal) {
            Box(contentAlignment = Alignment.CenterStart) {
                Icon(
                    painter = painterResource(id = action.id),
                    contentDescription = action.label?.let { stringResource(it) },
                    tint = color,
                    modifier = Modifier.size(size)
                )
                Text(
                    value,
                    modifier = Modifier.padding(start = size),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = color
                    )
                )
            }
        } else {
            Box(contentAlignment = Alignment.TopCenter) {
                Icon(
                    painter = painterResource(id = action.id),
                    contentDescription = action.label?.let { stringResource(it) },
                    tint = color,
                    modifier = Modifier.size(size)
                )
                Text(
                    value,
                    modifier = Modifier.padding(top = (size.value * .9).dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = color,
                    )
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVolumeControl() {
    PeerTheme {
        Column {
            PostIcon(
                UiAction.Like,
                "1"
            ) {}
            PostIcon(
                UiAction.Like,
                "1",
                orientation = Orientation.Vertical
            ) {}
        }
    }
}
