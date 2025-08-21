package eu.peernetwork.wallet.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.wallet.ui.R

@Composable
fun BoxScope.ToastLayout(
    modifier: Modifier = Modifier,
    size: Dp = 320.dp,
    rawRes: Int = R.raw.peerrocket,
    fillToWidthEdges: Boolean = true,
    overscanScale: Float = 1.12f,
    anchorBottom: Boolean = true
) {
    var isPlaying by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = 1,
        speed = 1f
    )
    LaunchedEffect(progress) {
        if (progress >= 1f) isPlaying = false
    }
    val aspect: Float = remember(composition) {
        val b = composition?.bounds
        val w = b?.width()?.toFloat() ?: 1f
        val h = b?.height()?.toFloat() ?: 1f
        if (h > 0f) w / h else 1f
    }
    AnimatedVisibility(
        visible = isPlaying && progress < 1f,
        enter = fadeIn(animationSpec = tween(160)),
        exit = fadeOut(animationSpec = tween(220)),
        modifier = modifier.matchParentSize()
    ) {
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = if (anchorBottom) Alignment.BottomCenter else Alignment.Center
        ) {
            val lottieMod = if (fillToWidthEdges) {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect, matchHeightConstraintsFirst = false)
                    .graphicsLayer {
                        scaleX = overscanScale
                        scaleY = overscanScale
                    }
            } else {
                Modifier.size(size)
            }

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = lottieMod
            )
        }
    }
    LaunchedEffect(Unit) {
        isPlaying = true
    }
}
