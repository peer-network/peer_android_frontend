package eu.peernetwork.core.ui.design.luna

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = MaterialTheme.colorScheme.onBackground,
    border: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    durationMillis: Int = 300,
    background: @Composable BoxScope.() -> Unit = {},
    easing: Easing = FastOutSlowInEasing,
    content: @Composable BoxScope.(Boolean) -> Unit
) {
    val handleCheckedChange by rememberUpdatedState(onCheckedChange)
    val updatedContent by rememberUpdatedState(content)
    val alpha by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = easing
        )
    )
    DesignBox(modifier = modifier.clip(shape)
        .border(
            width = 1.5.dp,
            color = border,
            shape = shape
        ).clickable { handleCheckedChange(!checked) },
        background = background
    ) {
        Box(modifier = Modifier.clip(shape)
            .background(backgroundColor))
        Box(modifier = Modifier.graphicsLayer {
            this.alpha = if (checked) alpha else 0f
        }) {
            CompositionLocalProvider(LocalContentColor provides if (enabled) {
                color
            } else {
                backgroundColor
            }) { updatedContent(checked) }
        }
    }
}

@Composable
@Preview
fun DesignCheckboxPreview() {
    DesignTheme {
        DesignCheckbox(checked = true, {}) {
            Box(modifier = Modifier.size(16.dp)) {
                Text(
                    "-",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
