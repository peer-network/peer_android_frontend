package eu.peernetwork.app.ui.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.search.member.MemberDialog
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.overview.OverviewScreen
import eu.peernetwork.wallet.ui.transfer.TransferScreen

@Composable
fun WalletScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Wallet.Builder::class.java).build(context)
    }
    val recipient = remember { mutableStateOf<UiRecipient?>(null) }
    WalletNavigation {
        WalletScreen(
            onRefresh = { },
            header = { OverviewScreen(component, viewModelStoreOwner) }
        ) {
            TransferScreen(recipient, component, viewModelStoreOwner) {

            }
        }
        DesignTitleBarHost("WalletScreen") {
            titleBar {
                DesignTitle {
                    Text(stringResource(R.string.wallet_label))
                }
            }
        }
        MemberDialog()
    }
}

@Composable
fun WalletScreen(
    onRefresh: () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    val state = remember { derivedStateOf { DesignStatefulScaffoldState.Success(Unit) } }
    DesignRefreshableScaffold<Unit>(state, onRefresh = onRefresh) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            updatedHeader()
            Spacer(modifier = Modifier.height(16.dp))
            updatedContent()
        }
    }
}
