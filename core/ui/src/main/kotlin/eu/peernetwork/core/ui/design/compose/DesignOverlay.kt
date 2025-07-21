package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DesignOverlay(
    state: State<Boolean>,
    modifier: Modifier = Modifier,
    startDestination: String? = null,
    propagateMinConstraints: Boolean = false,
    contentAlignment: Alignment = Alignment.BottomCenter,
    onDismiss: () -> Unit,
    content: @Composable (NavHostController) -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    DesignDialog(
        state = state,
        startDestination = startDestination,
        dim = true,
        onDismiss = onDismiss
    ) { controller, animation, cancelable ->
        Box(
            modifier = modifier,
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            val visibility = remember { mutableStateOf(false) }
            val offset = with(LocalDensity.current) { 64.dp.toPx() }
            Box(modifier = Modifier.graphicsLayer {
                    alpha = animation.value
                    translationY = (1 - animation.value) * offset
                }.background(MaterialTheme.colorScheme.background)
            ) { updatedContent(controller) }
            LaunchedEffect(state.value) {
                visibility.value = state.value
            }
        }
    }
}

@Composable
fun DesignOverlayPage(
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
