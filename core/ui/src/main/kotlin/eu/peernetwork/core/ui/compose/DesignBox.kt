package eu.peernetwork.core.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun FlexBox(
    modifier: Modifier = Modifier,
    minWidth: Float = 0.0f,
    minHeight: Float = 0.0f,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable () -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        modifier = modifier.defaultMinSize(
                minWidth = screenWidth * minWidth,
                minHeight = screenHeight * minHeight
            )
    ) { content() }
}
