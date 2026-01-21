package eu.peernetwork.user.ui.deactivate

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DeactivateScreen(
    show: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Deactivate.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = DeactivateViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is DeactivateViewModel.State.Loading } }
    val isSuccess = remember { derivedStateOf { state is DeactivateViewModel.State.Success } }
    val password = remember { TextFieldState() }
    val focus = remember { FocusRequester() }
    DesignBottomSheetScaffold(
        state = show,
        dismissable = true,
        color = MaterialTheme.colorScheme.surfaceDim,
        onShow = { focus.requestFocus() },
        onDismiss = { show.value = false }
    ) {
        DeactivateSheet(
            state = show,
            isLoading = isLoading,
            password = password,
            focus = focus,
            onCancel = { show.value = false }
        ) { viewModel(password.text.toString()) }
    }
    LaunchedEffect(isSuccess.value) {
        if (isSuccess.value) {
            show.value = false
            viewModel.reset()
        }
    }
}
