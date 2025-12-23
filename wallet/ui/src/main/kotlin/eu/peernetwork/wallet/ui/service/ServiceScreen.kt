package eu.peernetwork.wallet.ui.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTax

sealed interface ServiceState {
    data object Default : ServiceState
    data class Transfer(val recipient: UiRecipient) : ServiceState
}

@Composable
fun ServiceScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (State<DesignStreamState<UiTax>>, () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Service.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ServiceViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            ServiceViewModel.State.Empty -> DesignStreamState.Default
            ServiceViewModel.State.Loading -> DesignStreamState.Loading
            is ServiceViewModel.State.Success -> {
                val data = (state as ServiceViewModel.State.Success)
                DesignStreamState.Success(data.tax)
            }
            is ServiceViewModel.State.Error -> {
                DesignStreamState.Error((state as ServiceViewModel.State.Error).error)
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    val handleRefresh by rememberUpdatedState { viewModel.initialize() }
    updatedContent(derivedState, handleRefresh)
    LaunchedEffect(Unit) {
        if (state is ServiceViewModel.State.Empty) {
            viewModel.initialize()
        }
    }
}

@Composable
fun ServiceScreen(
    serviceState: MutableState<ServiceState>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAccountClicked: (String) -> Unit,
    onClear: () -> Unit,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Service.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ServiceViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            ServiceViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            ServiceViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is ServiceViewModel.State.Success -> {
                val data = (state as ServiceViewModel.State.Success)
                DesignStatefulScaffoldState.Success(data.tax)
            }
            is ServiceViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as ServiceViewModel.State.Error).error)
            }
        }
    } }
    DesignStatefulScaffold<UiTax>(
        derivedState,
        onRefresh = { viewModel.initialize() },
        placeholder = { ServicePage() },
        errorContent = { ServiceError(it, component.resource()) { viewModel.initialize() } }
    ) {
        ServiceTransfer(
            tax = ((it.burn + it.peer) * 100).toInt(),
            serviceState,
            component,
            viewModelStoreOwner,
            onAccountClicked,
            onClear,
            onClick
        )
    }
}

