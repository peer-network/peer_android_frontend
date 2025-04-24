package eu.peernetwork.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.app.R

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    SplashScaffold(onFinished = onAnimationFinished)
}

@Composable
private fun SplashScaffold(onFinished: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_animation)
    )
    var playForward by remember { mutableStateOf(true) }
    val progressAnim = remember { Animatable(0f) }
    LaunchedEffect(composition, playForward) {
        if (composition == null) return@LaunchedEffect
        progressAnim.animateTo(
            targetValue = if (playForward) 1f else 0f,
            animationSpec = tween(
                durationMillis = composition!!.duration.toInt(),
                easing = LinearEasing
            )
        )

        if (playForward) {
            playForward = false
        } else {
            onFinished()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progressAnim.value },
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
