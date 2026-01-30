package eu.peernetwork.app.ui.wallet

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.interactor.NavigationInteractor
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
import eu.peernetwork.wallet.ui.saveable.UiRecipientSaver
import eu.peernetwork.wallet.ui.transactions.TransactionsList
import eu.peernetwork.wallet.ui.transactions.TransactionsNavigator
import kotlinx.coroutines.launch

@Composable
fun WalletScreen(
    account: Account,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val component = remember {
        provider.builder(Wallet.Builder::class.java).build(context)
    }
    val recipient = rememberSaveable(saver = UiRecipientSaver) {
        mutableStateOf(null)
    }
    val listState = rememberLazyListState()
    val controller = rememberNavController()
    val focusRequester = remember { FocusRequester() }
    val showSheet = rememberSaveable { mutableStateOf(false) }
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val navigator = remember { NavigationInteractor(context, controller) }
    CompositionLocalProvider(TransactionsNavigator.LocalTransactionsNavigator provides navigator) {
        WalletNavigation(
            account = account,
            disable = showSheet,
            recipient = recipient,
            focusRequester = focusRequester,
            controller = controller,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onSearch = { showSheet.value = true }
        ) { controller ->
            WalletPage(
                onClick = {
                    recipient.value = null
                    controller.navigateIfNecessary("transfer")
                },
                onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
                header = { BalanceOverview(lastUpdated, component, viewModelStoreOwner) },
            ) {
                TransactionsList(
                    uuid = account.id,
                    limit = postLimit,
                    lastUpdated = lastUpdated,
                    listState = listState,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) { controller.navigateIfNecessary("profile/${it}") }
            }
            DesignTitleBarHost(
                tag = "WalletScreen",
                listener = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                titleBar {
                    DesignTitle {
                        Text(stringResource(R.string.wallet_label))
                    }
                }
            }
        }
        MemberModal(
            postLimit = postLimit,
            showSheet = showSheet,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
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
