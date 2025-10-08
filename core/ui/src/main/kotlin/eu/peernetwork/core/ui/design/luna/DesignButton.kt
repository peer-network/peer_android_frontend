package eu.peernetwork.core.ui.design.luna

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = CircleShape,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.SemiBold
    ),
    colors: ButtonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.surfaceTint
    ),
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    minHeight: Dp = 48.dp,
    minWidth: Dp = 64.dp,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 16.dp,
        horizontal = 24.dp
    ),
    loading: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val clickHandler by rememberUpdatedState(onClick)
    val updatedLoading by rememberUpdatedState(loading)
    val updatedContent by rememberUpdatedState(content)
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (enabled) {
                colors.containerColor
            } else {
                colors.disabledContainerColor
            })
            .then(if (border != null) {
                Modifier.border(border, shape)
            } else {
                Modifier
            }).shadow(elevation = elevation, shape = shape)
            .clickable(
                role = Role.Button,
                enabled = enabled && !isLoading,
                onClick = { if (!isLoading && enabled) clickHandler() }
            ).defaultMinSize(minWidth = minWidth, minHeight = minHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis, easing = easing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Box(
                modifier = Modifier.graphicsLayer {
                    this.alpha = if (isLoading) alpha else 0f },
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides style.copy(
                        color = contentColor
                    )
                ) { updatedLoading() }
            }
            Box(modifier = Modifier.graphicsLayer {
                this.alpha = if (!isLoading) 1f else 0f }) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides style
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

@Preview
@Composable
fun DarkPreviewDesignButton() {
    DesignTheme(isDarkMode = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            DesignButton(
                onClick = {},
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                isLoading = true,
                loading = { Text("Loading...") }
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.sizeIn(minHeight = 56.dp, minWidth = 200.dp)
            ) { Text("Large Button") }
        }
    }
}

@Preview
@Composable
fun LightPreviewDesignButton() {
    DesignTheme(isDarkMode = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            DesignButton(
                onClick = {},
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                isLoading = true,
                loading = { Text("Loading...") }
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.sizeIn(minHeight = 56.dp, minWidth = 200.dp)
            ) { Text("Large Button") }
        }
    }
}

