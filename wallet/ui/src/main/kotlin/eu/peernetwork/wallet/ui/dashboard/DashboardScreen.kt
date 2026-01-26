package eu.peernetwork.wallet.ui.dashboard

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

sealed interface DashboardState {
    data object Default : DashboardState
    data class Transfer(val recipient: UiRecipient) : DashboardState
}

@Composable
fun DashboardScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (State<DesignStreamState<UiTax>>, () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Dashboard.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DashboardViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            DashboardViewModel.State.Empty -> DesignStreamState.Default
            DashboardViewModel.State.Loading -> DesignStreamState.Loading
            is DashboardViewModel.State.Success -> {
                val data = (state as DashboardViewModel.State.Success)
                DesignStreamState.Success(data.tax)
            }
            is DashboardViewModel.State.Error -> {
                DesignStreamState.Error((state as DashboardViewModel.State.Error).error)
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    val handleRefresh by rememberUpdatedState { viewModel.initialize() }
    updatedContent(derivedState, handleRefresh)
    LaunchedEffect(Unit) {
        if (state is DashboardViewModel.State.Empty) {
            viewModel.initialize()
        }
    }
}

@Composable
fun DashboardScreen(
    dashboardState: MutableState<DashboardState>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAccountClicked: (String) -> Unit,
    onClear: () -> Unit,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Dashboard.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DashboardViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            DashboardViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            DashboardViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is DashboardViewModel.State.Success -> {
                val data = (state as DashboardViewModel.State.Success)
                DesignStatefulScaffoldState.Success(data.tax)
            }
            is DashboardViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as DashboardViewModel.State.Error).error)
            }
        }
    } }
    DesignStatefulScaffold<UiTax>(
        derivedState,
        onRefresh = { viewModel.initialize() },
        placeholder = { DashboardPage() },
        errorContent = { DashboardError(it, component.resource()) { viewModel.initialize() } }
    ) {
        DashboardTransfer(
            tax = ((it.burn + it.peer) * 100).toInt(),
            dashboardState,
            component,
            viewModelStoreOwner,
            onAccountClicked,
            onClear,
            onClick
        )
    }
}

