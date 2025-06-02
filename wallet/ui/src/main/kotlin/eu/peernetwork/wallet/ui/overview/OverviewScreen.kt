package eu.peernetwork.wallet.ui.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiWallet

@Composable
fun OverviewScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Overview.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = OverviewViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                OverviewViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                OverviewViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is OverviewViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as OverviewViewModel.State.Success).wallet
                    )
                }
                is OverviewViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as OverviewViewModel.State.Error).error
                )
            }
        }
    }
    DesignStatefulScaffold<UiWallet>(
        state = derivedState,
        onRefresh = { viewModel.getBalance() },
        placeholder = { OverviewScaffold() }
    ) { OverviewScreen(it) }
}

@Composable
fun OverviewScreen(wallet: UiWallet) {
    OverviewScaffold(
        title = {
            Text(
                text = "${wallet.balance}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    ) {
        Text(
            text = "Each token is ${wallet.rate}${wallet.currency}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Now you own ${wallet.converted}${wallet.currency}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center
        )
    }
}
