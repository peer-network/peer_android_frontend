package eu.peernetwork.user.ui.v2.password.reset

import androidx.compose.runtime.Composable
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
fun ResetScreen(
    token: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Reset.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ResetViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val response = remember { derivedStateOf { state as? ResetViewModel.State.Success? } }
    val isLoading = remember(state) { derivedStateOf {
        state is ResetViewModel.State.Loading
    } }
    val error = remember(state) { derivedStateOf {
        (state as? ResetViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    val handleOnReset by rememberUpdatedState(onReset)
    ResetPage(
        isLoading = isLoading,
        error = error
    ) { viewModel.reset(token, it) }
    LaunchedEffect(response.value) {
        response.value?.let { handleOnReset() }
    }
}
