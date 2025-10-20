package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerAppGray85
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.core.ui.theme.PeerTheme

data class DesignIndicatorColors(
    val bad: Color,
    val weak: Color,
    val medium: Color,
    val good: Color,
    val strong: Color,
    val excellent: Color
) {
    companion object {
        val default = DesignIndicatorColors(
            bad = PeerAppGray85,
            weak = PeerAppRed,
            medium = PeerAppYellow,
            good = PeerAppYellow,
            strong = PeerAppLightGreen,
            excellent = PeerAppGreen,
        )
    }
}

@Composable
fun DesignIndicator(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeColor: Color,
    inActiveColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RoundedCornerShape(16.dp),
    durationMillis: Int = 10,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing,
) {
    val color by animateColorAsState(
        targetValue = if (isActive) { activeColor } else { inActiveColor },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    Box(modifier = modifier.clip(shape).background(color))
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewIndicator() {
    PeerTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) {
                DesignIndicator(
                    isActive = it % 2 == 0,
                    modifier = Modifier.weight(1f).height(8.dp),
                    activeColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
