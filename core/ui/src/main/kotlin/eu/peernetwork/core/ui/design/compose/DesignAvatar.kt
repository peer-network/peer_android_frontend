package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DesignAvatar(
    modifier: Modifier = Modifier,
    angle: Float = 45f,
    icon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val updatedIcon by rememberUpdatedState(icon)
    val updatedContent by rememberUpdatedState(content)
    Layout(
        content = {
            Box(modifier = Modifier.clip(CircleShape)) { updatedContent() }
            Box { updatedIcon?.invoke() }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val contentPlaceable = measurables[0].measure(constraints)
        val contentRadius = contentPlaceable.width / 2f
        val badgePlaceable = measurables[1].measure(Constraints())
        val badgeRadius = badgePlaceable.width / 2f
        val layoutSize = contentPlaceable.width
        val angleRad = Math.toRadians(angle.toDouble())
        val badgeX = (contentRadius + cos(angleRad) * contentRadius - badgeRadius).toInt()
        val badgeY = (contentRadius + sin(angleRad) * contentRadius - badgeRadius).toInt()
        layout(layoutSize, layoutSize) {
            contentPlaceable.placeRelative(0, 0)
            badgePlaceable.placeRelative(badgeX, badgeY)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignAvatar() {
    PeerTheme {
        DesignAvatar(
            icon = { Box(
                modifier = Modifier.size(18.dp)
                    .background(Color.Green, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
            ) }
        ) {
            Box(modifier = Modifier.size(80.dp)
                .background(MaterialTheme.colorScheme.onSurface))
        }
    }
}
