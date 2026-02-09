package eu.peernetwork.wallet.ui.rate

import androidx.compose.runtime.Composable
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
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiTax

@Composable
fun RateScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (State<DesignStreamState<UiTax>>, () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Rate.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = RateViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            RateViewModel.State.Empty -> DesignStreamState.Default
            RateViewModel.State.Loading -> DesignStreamState.Loading
            is RateViewModel.State.Success -> {
                val data = (state as RateViewModel.State.Success)
                DesignStreamState.Success(data.tax)
            }
            is RateViewModel.State.Error -> {
                DesignStreamState.Error((state as RateViewModel.State.Error).error)
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    val handleRefresh by rememberUpdatedState { viewModel.initialize() }
    updatedContent(derivedState, handleRefresh)
    LaunchedEffect(Unit) {
        if (state is RateViewModel.State.Empty) {
            viewModel.initialize()
        }
    }
}
