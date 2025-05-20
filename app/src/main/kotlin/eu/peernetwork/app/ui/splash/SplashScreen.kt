package eu.peernetwork.app.ui.splash

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignErrorContent
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun SplashScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAnimationFinished: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Splash.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = SplashViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showDialog = remember { mutableStateOf(false) }
    val updateUrl = remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is SplashViewModel.State.Outdated) {
            updateUrl.value = (state as SplashViewModel.State.Outdated).url
            showDialog.value = true
        }
    }

    val derivedState = remember {
        mutableStateOf<DesignStatefulScaffoldState>(DesignStatefulScaffoldState.Empty)
    }
    var play = remember { mutableStateOf(true) }
    val onFinish by rememberUpdatedState(onAnimationFinished)
    DesignStatefulScaffold<Unit>(
        state = derivedState,
        onRefresh = {
            derivedState.value = DesignStatefulScaffoldState.Success(Unit)
            play.value = true
            viewModel.initialize() },
        errorContent = { DesignErrorContent(
            it,
            onRetry = {
                derivedState.value = DesignStatefulScaffoldState.Success(Unit)
                play.value = true
                viewModel.initialize()
                      },
            modifier = Modifier.fillMaxSize()
            ) }
    ) { SplashScreen(play) {
        if (it == AnimationEndReason.Finished && state is SplashViewModel.State.Ready) {
            if (!play.value) {
                onFinish()
            }
            play.value = false
        } else if (it == AnimationEndReason.Finished && state is SplashViewModel.State.Error) {
            if (!play.value) {
                derivedState.value = DesignStatefulScaffoldState.Error(
                    (state as SplashViewModel.State.Error).error
                )
            }
            play.value = false
        }
    } }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Update Required") },
            text = { Text("This version of the app is outdated. Please update to continue.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl.value))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Update Now")
                }
            },
            dismissButton = {}
        )
    }
}

@Composable
fun SplashScreen(play: State<Boolean>, onAnimationFinished: (AnimationEndReason) -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_animation)
    )
    val progressAnim = remember { Animatable(0f) }
    val onFinish by rememberUpdatedState(onAnimationFinished)
    LaunchedEffect(composition, play.value) {
        if (composition == null) return@LaunchedEffect
        val progress = progressAnim.animateTo(
            targetValue = if (play.value) 1f else 0f,
            animationSpec = tween(
                durationMillis = composition!!.duration.toInt(),
                easing = LinearEasing
            )
        )
        onFinish(progress.endReason)
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
