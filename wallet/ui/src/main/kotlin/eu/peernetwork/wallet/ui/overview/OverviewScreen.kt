package eu.peernetwork.wallet.ui.overview

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.model.UiWallet
import java.math.BigDecimal

@Composable
fun OverviewScreen(
    lastUpdated: State<Long>,
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
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    DesignStatefulScaffold<UiWallet>(
        state = derivedState,
        onRefresh = { viewModel.getBalance() },
        placeholder = { OverviewScaffold() }
    ) { OverviewScreen(it) }
    LaunchedEffect(lastUpdated.value) {
        if (updatedAt.longValue != lastUpdated.value) {
            viewModel.getBalance()
            updatedAt.longValue = lastUpdated.value
        }
    }
}

@Composable
fun OverviewScreen(wallet: UiWallet) {
    OverviewScaffold(
        rate = {
            Text(
                text = wallet.rate.toString(),
                color = PeerAppGreen,
                textAlign = TextAlign.Center,
            )
        },
        token = {
            Text(
                text = wallet.balance.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }
    ) {
        Text(
            text = "~ ${wallet.converted}${wallet.currency}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewOverviewScreen() {
    PeerTheme {
        OverviewScreen(
            UiWallet(
                balance = BigDecimal(1000),
                rate = 0.1f,
                converted = BigDecimal(10),
                currency = "$"
            )
        )
    }
}
