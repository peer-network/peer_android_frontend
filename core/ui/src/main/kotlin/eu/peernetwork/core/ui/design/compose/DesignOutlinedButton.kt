package eu.peernetwork.core.ui.design.compose

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 0.dp,
    durationMillis: Int = 1000,
    minWidth: Dp = ButtonDefaults.MinWidth,
    minHeight: Dp = ButtonDefaults.MinHeight,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.tertiary,
        disabledContentColor = MaterialTheme.colorScheme.surfaceTint
    ),
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceTint),
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    content: @Composable () -> Unit
) {
    val onClickState by rememberUpdatedState(onClick)
    val updateContent by rememberUpdatedState(content)
    val clickHandler = remember(isLoading, enabled) { { if (!isLoading && enabled) onClickState() } }
    val backgroundColor by remember(enabled) { derivedStateOf {
        if (enabled) {
            colors.containerColor
        } else {
            colors.disabledContainerColor
        }
    } }
    val contentColor by remember { derivedStateOf {
        if (enabled) {
            colors.contentColor
        } else {
            colors.disabledContentColor
        }
    } }
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(border, shape)
            .shadow(elevation, shape, clip = false)
            .clickable(
                role = Role.Button,
                enabled = enabled && !isLoading,
                onClick = clickHandler
            )
            .defaultMinSize(
                minWidth = minWidth,
                minHeight = minHeight
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
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
                    modifier = Modifier.graphicsLayer { this.alpha = alpha },
                    style = textStyle.copy(color = contentColor)
                )
            } else {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides textStyle
                ) { updateContent() }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignOutlinedButton() {
    PeerTheme {
        Column {
            DesignOutlinedButton(
                onClick = {},
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Follow")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = (painterResource(id = R.drawable.ic_like)),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Transparent)
                    )
                }
            }
            DesignOutlinedButton(
                onClick = {},
                isLoading = true,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text("Follow")
            }
        }
    }
}
