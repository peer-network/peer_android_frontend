package eu.peernetwork.wallet.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignScreenScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulContentPlaceholder
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiWallet
import java.math.BigDecimal

@Composable
fun OverviewScreen(
    title: MutableState<DesignToolbarTitle>,
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
    DesignScreenScaffold<UiWallet>(
        state = derivedState,
        onRefresh = { viewModel.getBalance() },
        placeholder = { DesignStatefulContentPlaceholder(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())) }
    ) {
        DesignRefreshableScaffold<UiWallet>(
            state = derivedState,
            onRefresh = { viewModel.getBalance() },
        ) { wallet ->
            OverviewScreen(wallet)
        }
    }
    LaunchedEffect(Unit) {
        title.value = DesignToolbarTitle(
            eu.peernetwork.core.ui.R.string.wallet_label
        )
    }
}

@Composable
fun OverviewScreen(wallet: UiWallet) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 16.dp,
                horizontal = 24.dp,
            ).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.weight(.1f))
        Column(
            modifier = Modifier.weight(.9f)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_icon),
                    contentDescription = "wallet_logo",
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${wallet.balance}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Each token is ${wallet.rate}${wallet.currency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Now you own ${wallet.converted}${wallet.currency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
