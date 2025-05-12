package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun  DesignDialog(
    tag: String,
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    durationMillis: Int = DefaultDurationMillis,
    enter: EnterTransition = fadeIn(animationSpec = tween(durationMillis = durationMillis)),
    exit: ExitTransition = fadeOut(animationSpec = tween(durationMillis = durationMillis)),
    contentAlignment: Alignment = Alignment.TopStart,
    background: (@Composable () -> Unit)? = null,
    builder: @Composable (State<Boolean>) -> Unit,
) {
    val updatedBackground by rememberUpdatedState(background)
    val updatedBuilder by rememberUpdatedState(builder)
    DesignOverlayHost(
        tag = tag,
        visible = visible,
        durationMillis = durationMillis
    ) { dialogState ->
        overlay {
            Box(
                modifier = modifier,
                contentAlignment = contentAlignment
            ) {
                updatedBackground?.invoke() ?: DesignOverlayBackground(
                    state = dialogState,
                    durationMillis = durationMillis,
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
                AnimatedVisibility(
                    visible = dialogState.value,
                    enter = enter,
                    exit = exit
                ) { updatedBuilder(dialogState) }
            }
        }
    }
}
