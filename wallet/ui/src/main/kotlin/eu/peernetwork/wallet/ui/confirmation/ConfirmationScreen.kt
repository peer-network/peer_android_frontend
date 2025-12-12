package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error
import eu.peernetwork.wallet.ui.model.v2.UiQuote
import eu.peernetwork.wallet.ui.model.v2.UiToken

@Composable
fun ConfirmationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Confirmation.Component, ConfirmationViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Confirmation.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ConfirmationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
fun ConfirmationScreen(
    token: UiToken,
    component: Confirmation.Component,
    viewModel: ConfirmationViewModel,
    onCancel: () -> Unit,
    content: @Composable (State<UiQuote>) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            ConfirmationViewModel.State.Empty -> DesignStreamState.Default
            ConfirmationViewModel.State.Loading -> DesignStreamState.Loading
            is ConfirmationViewModel.State.Success -> {
                DesignStreamState.Success((state as ConfirmationViewModel.State.Success).quote)
            }
            is ConfirmationViewModel.State.Error -> {
                DesignStreamState.Error((state as ConfirmationViewModel.State.Error).error)
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    DesignStream(
        state = derivedState,
        default = { ConfirmationSkeleton() },
        loading = { ConfirmationSkeleton() },
        error = { error ->
            ConfirmationError(
                error = component.resource().error(error.value),
                onCancel = onCancel,
            ) { viewModel(token) }
        }
    ) { data ->
        updatedContent(data)
    }
    LaunchedEffect(Unit) {
        if (state is ConfirmationViewModel.State.Empty) {
            viewModel(token)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.reset()
        }
    }
}
