package eu.peernetwork.core.ui.compose

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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(22.dp),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    durationMillis: Int = 1000,
    border: BorderStroke = BorderStroke(1.dp, Color.DarkGray),
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = null,
    icon: Painter? = null,
    content: @Composable () -> Unit,
    ) {

    val onClickState by rememberUpdatedState(onClick)
    val clickHandler = remember { { if (!isLoading) onClickState() } }

    OutlinedButton(
        onClick = clickHandler,
        modifier = modifier
            .height(44.dp)
            .clip(shape),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = Color.DarkGray
        ),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        if (isLoading) {
            val color = Color.DarkGray
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
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        } else {
            content()
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
                icon = painterResource(id = R.drawable.ic_like)
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
                icon = painterResource(id = R.drawable.ic_add)
            ) {
                Text("Follow")
            }
        }
    }
}
