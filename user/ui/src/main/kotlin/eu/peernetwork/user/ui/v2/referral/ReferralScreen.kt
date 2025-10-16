package eu.peernetwork.user.ui.v2.referral

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignDialog
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ReferralScreen(
    referral: String? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRegister: (String) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Referral.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ReferralViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val status by viewModel.status.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is ReferralViewModel.State.Loading } }
    val isLoadingReferral = remember { derivedStateOf {
        status is ReferralViewModel.Status.Loading
    } }
    val derivedState = remember {
        derivedStateOf { (state as? ReferralViewModel.State.Success)?.referral }
    }
    val peerReferral = remember { derivedStateOf {
        (status as? ReferralViewModel.Status.Success?)?.referral
    } }
    val code = remember(peerReferral.value) { TextFieldState(if (referral == "null") {
        peerReferral.value
    } else {
        referral ?: peerReferral.value
    } ?: "") }
    val error = remember { mutableStateOf<String?>(null) }
    val derivedStatusError = remember {
        derivedStateOf { (status as? ReferralViewModel.Status.Error?)?.error }
    }
    val derivedStateError = remember {
        derivedStateOf { (state as? ReferralViewModel.State.Error?)?.error }
    }
    ReferralPage(
        code = code,
        isLoading = isLoading,
        error = error,
        onRequestReferral = {
            peerReferral.value?.let {
                error.value = null
                code.edit {
                    replace(0, length, it)
                }
            } ?: viewModel.getDefaultReferral()
        },
        onVerify = {
            error.value = null
            viewModel.verify(it)
        }
    )
    DesignDialog(
        isLoadingReferral,
        dim = true,
        onDismiss = { }
    ) { controller, anim, cancelable ->
        val infiniteTransition = rememberInfiniteTransition()
        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                this.alpha = alpha
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = stringResource(R.string.loading_text),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
            )
        }
    }
    LaunchedEffect(derivedState.value) {
        derivedState.value?.let { onRegister(it) }
    }
    LaunchedEffect(derivedStatusError.value) {
        derivedStatusError.value?.message?.let {
            error.value = component.resource().string(it)
        }
    }
    LaunchedEffect(derivedStateError.value) {
        derivedStateError.value?.message?.let {
            error.value = component.resource().string(it)
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
