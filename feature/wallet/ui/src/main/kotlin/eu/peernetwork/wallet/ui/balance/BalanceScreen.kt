package eu.peernetwork.wallet.ui.balance

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
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiWallet

@Composable
fun BalanceScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Balance.Component, BalanceViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Balance.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = BalanceViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
fun BalanceScreen(
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    loading: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable>) -> Unit = {},
    content: @Composable (State<UiWallet>) -> Unit
) {
    BalanceScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    BalanceViewModel.State.Empty -> DesignStreamState.Default
                    BalanceViewModel.State.Loading -> DesignStreamState.Loading
                    is BalanceViewModel.State.Success -> {
                        DesignStreamState.Success(
                            (state as BalanceViewModel.State.Success).wallet
                        )
                    }
                    is BalanceViewModel.State.Error -> DesignStreamState.Error(
                        (state as BalanceViewModel.State.Error).error
                    )
                }
            }
        }
        val updatedContent by rememberUpdatedState(content)
        DesignStream(
            state = derivedState,
            loading = loading,
            error = error
        ) { updatedContent(it) }
        LaunchedEffect(lastUpdated.value) {
            if (state is BalanceViewModel.State.Empty) {
                viewModel()
            }
        }
    }
}
