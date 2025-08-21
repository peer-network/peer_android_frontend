package eu.peernetwork.wallet.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.wallet.ui.R

@Composable
fun TickButton(
    modifier: Modifier = Modifier,
    rawRes: Int = R.raw.tick
) {
    var playing by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = playing,
        iterations = 1,
        speed = 1f
    )
    LaunchedEffect(Unit) {
        playing = true
    }
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}
