package eu.peernetwork.app.ui.splash

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
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
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import androidx.core.net.toUri
import eu.peernetwork.app.ui.compose.UpdateDialog

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
    val derivedState = remember {
        mutableStateOf<DesignStatefulScaffoldState>(DesignStatefulScaffoldState.Empty)
    }
    var play = remember { mutableStateOf(true) }
    val isLoading = remember(state) { derivedStateOf { state is SplashViewModel.State.Loading } }
    val isReady = remember(state) { derivedStateOf {
        (state as? SplashViewModel.State.Success?)?.let {
            it.update == null
        } == true
    } }
    val onFinish by rememberUpdatedState(onAnimationFinished)
    DesignStatefulScaffold<Unit>(
        state = derivedState,
        onRefresh = {
            derivedState.value = DesignStatefulScaffoldState.Success(Unit)
            play.value = true
            viewModel.initialize() },
        errorContent = { DesignError(
            it,
            onRetry = {
                derivedState.value = DesignStatefulScaffoldState.Success(Unit)
                play.value = true
                viewModel.initialize() },
            modifier = Modifier.fillMaxSize()
            ) }
    ) { SplashScreen(play, isLoading) {
        if (it == AnimationEndReason.Finished && isReady.value) {
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
    UpdateDialog(showDialog, updateUrl.value.toUri())
    LaunchedEffect(state) {
        (state as? SplashViewModel.State.Success?)?.update?.let {
            updateUrl.value = it
            showDialog.value = true
        }
    }
}

@Composable
fun SplashScreen(
    play: State<Boolean>,
    isReady: State<Boolean>,
    onAnimationFinished: (AnimationEndReason) -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_animation)
    )
    val progressAnim = remember { Animatable(0f) }
    val onFinish by rememberUpdatedState(onAnimationFinished)
    LaunchedEffect(composition, play.value, isReady.value) {
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
        Crossfade(composition) { target ->
            if (target != null) {
                LottieAnimation(
                    composition = target,
                    progress = { progressAnim.value },
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
