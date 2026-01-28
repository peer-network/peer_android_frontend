package eu.peernetwork.app.ui.wallet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.search.member.MemberModal
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.wallet.ui.balance.BalanceOverview
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.dashboard.DashboardScreen
import eu.peernetwork.wallet.ui.dashboard.DashboardState
import eu.peernetwork.wallet.ui.saveable.UiRecipientSaver
import eu.peernetwork.wallet.ui.transactions.TransactionsList

@Composable
fun WalletScreen(
    account: Account,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Wallet.Builder::class.java).build(context)
    }
    val recipient = rememberSaveable(saver = UiRecipientSaver) {
        mutableStateOf(null)
    }
    val service = remember(recipient.value) {
        mutableStateOf(recipient.value?.let {
            DashboardState.Transfer(it)
        } ?: DashboardState.Default)
    }
    val showSheet = rememberSaveable { mutableStateOf(false) }
    val lastUpdated = remember { mutableLongStateOf(System.currentTimeMillis()) }
    WalletNavigation(
        account = account,
        provider = component
    ) { controller ->
        WalletPage(
            onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
            header = { BalanceOverview(lastUpdated, component, viewModelStoreOwner) },
            transactions = {
                TransactionsList(
                    uuid = account.id,
                    limit = postLimit,
                    lastUpdated = lastUpdated,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                )
            }
        ) {
            DashboardScreen(
                dashboardState = service,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onAccountClicked = { controller.navigateIfNecessary("profile/${it}") },
                onClear = { recipient.value = null }
            ) { showSheet.value = true }
        }
        DesignTitleBarHost("WalletScreen") {
            titleBar {
                DesignTitle {
                    Text(stringResource(R.string.wallet_label))
                }
            }
        }
        MemberModal(postLimit, showSheet, component, viewModelStoreOwner) {
            recipient.value = UiRecipient(
                id = it.id,
                username = it.username,
                slug = it.slug,
                imageUrl = it.imageUrl
            )
            true
        }
    }
}
