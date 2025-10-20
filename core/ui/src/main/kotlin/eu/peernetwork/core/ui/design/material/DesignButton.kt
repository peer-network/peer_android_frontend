package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: ButtonColors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.secondary
    ),
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    minHeight: Dp = 48.dp,
    minWidth: Dp = 64.dp,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    content: @Composable () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary,
        )
    )
    val clickHandler by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(content)
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush = gradient)
            .then(if (border != null) {
                Modifier.border(border, shape)
            } else {
                Modifier
            }).shadow(elevation = elevation, shape = shape)
            .clickable(
                role = Role.Button,
                enabled = enabled && !isLoading,
                onClick = { if (!isLoading && enabled) clickHandler() }
            )
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis, easing = easing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Text(
                text = stringResource(id = R.string.loading_text),
                modifier = Modifier.graphicsLayer {
                    this.alpha = if (isLoading) alpha else 0f },
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            )
            Box(modifier = Modifier.graphicsLayer {
                this.alpha = if (!isLoading) 1f else 0f }) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor
                ) { updatedContent() }
            }
        }
    }
}

data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignButton() {
    PeerTheme {
        Column {
            DesignButton(
                onClick = {},
                modifier = Modifier.padding(16.dp)
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                isLoading = true,
                modifier = Modifier.padding(16.dp)
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .padding(16.dp)
                    .sizeIn(minHeight = 80.dp, minWidth = 200.dp)
            ) { Text("Large Button") }
        }
    }
}
