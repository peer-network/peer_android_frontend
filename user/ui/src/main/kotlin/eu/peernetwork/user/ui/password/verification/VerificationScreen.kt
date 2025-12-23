package eu.peernetwork.user.ui.password.verification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun VerificationScreen(
    email: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onVerification: (String) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Verification.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VerificationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val response = remember { derivedStateOf { state as? VerificationViewModel.State.Success? } }
    val isLoading = remember(state) { derivedStateOf {
        state is VerificationViewModel.State.Loading
    } }
    val error = remember(state) { derivedStateOf {
        (state as? VerificationViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    val handleOnVerification by rememberUpdatedState(onVerification)
    VerificationPage(
        email = email,
        isLoading = isLoading,
        error = error,
    ) { viewModel(it) }
    LaunchedEffect(response.value) {
        response.value?.let { handleOnVerification(it.token) }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
