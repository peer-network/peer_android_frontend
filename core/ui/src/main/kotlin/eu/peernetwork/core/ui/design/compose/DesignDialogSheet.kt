package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier

@Composable
fun DesignDialogSheet(
    tag: String,
    visible: Boolean = false,
    durationMillis: Int = DefaultDurationMillis,
    builder: @Composable (State<Boolean>) -> Unit,
) {
    DesignOverlayHost(tag = tag, visible = visible) {
        overlay {
            DesignOverlayBackground(
                state = it,
                durationMillis = durationMillis,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
            AnimatedVisibility(
                visible = it.value,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = durationMillis)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = durationMillis)
                ) + fadeOut()
            ) { builder(it) }
        }
    }
}
