package eu.peernetwork.app.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import eu.peernetwork.core.ui.design.compose.DesignTitleBar

@Composable
fun ContentOverlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignTitleBar {
        Box(modifier = modifier) {
            updatedContent()
        }
    }
}
