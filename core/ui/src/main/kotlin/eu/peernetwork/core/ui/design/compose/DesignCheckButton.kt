package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignCheckButton(
    check: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onCheck: (Boolean) -> Unit,
    shape: Shape = RoundedCornerShape(16.dp),
    enabled: Boolean = true,
    colors: ButtonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.tertiary,
        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ),
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    minHeight: Dp = 32.dp,
    minWidth: Dp = 32.dp,
    durationMillis: Int = 400,
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    content: @Composable () -> Unit
    ) {
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val backgroundColor by animateColorAsState(
        targetValue = if (check.value) colors.containerColor else colors.disabledContainerColor,
        animationSpec = tween(durationMillis = durationMillis, easing = easing)
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (check.value) 1f else .6f,
        animationSpec = tween(durationMillis = durationMillis, easing = easing)
    )
    Box(
        modifier = modifier.clip(shape)
            .then(if (border != null) {
                Modifier.border(border, shape)
            } else {
                Modifier
            }).shadow(elevation = elevation, shape = shape)
            .background(backgroundColor)
            .clickable(
                role = Role.Button,
                enabled = enabled,
                onClick = {
                    if (enabled) {
                        check.value = !check.value
                        onCheck(check.value)
                    }
                }
            )
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(contentPadding)
                .graphicsLayer { alpha = animatedAlpha },
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides contentColor
            ) { content() }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignCheckButton() {
    PeerTheme {
        DesignCheckButton(
            check = remember { mutableStateOf(true) },
            onCheck = {}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
