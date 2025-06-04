package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun  DesignDialogSheet(
    tag: String,
    modifier: Modifier = Modifier,
    visible: MutableState<Boolean>,
    durationMillis: Int = DefaultDurationMillis,
    contentAlignment: Alignment = Alignment.TopStart,
    onAnimationComplete: (Boolean) -> Unit = {},
    background: (@Composable () -> Unit)? = null,
    builder: @Composable (State<Boolean>) -> Unit,
) {
    DesignDialog(
        tag = tag,
        modifier = modifier,
        visible = visible,
        onAnimationComplete = onAnimationComplete,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeOut(),
        durationMillis = durationMillis,
        contentAlignment =  contentAlignment,
        background = background,
        builder = builder
    )
}
