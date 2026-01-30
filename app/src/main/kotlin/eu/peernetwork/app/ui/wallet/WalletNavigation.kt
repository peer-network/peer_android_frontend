package eu.peernetwork.app.ui.wallet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.transfer.v2.TransferScreen

@Composable
fun WalletNavigation(
    account: Account,
    disable: MutableState<Boolean>,
    recipient: MutableState<UiRecipient?>,
    focusRequester: FocusRequester,
    controller: NavHostController,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSearch: () -> Unit,
    content: @Composable (NavHostController) -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "wallet"
    ) {
        composable("wallet") { updatedContent(controller) }
        composable("transfer") {
            TransferScreen(
                disable = disable,
                recipient = recipient,
                provider = provider,
                focusRequester = focusRequester,
                viewModelStoreOwner = viewModelStoreOwner,
                onUserClicked = { controller.navigateIfNecessary("profile/${it}") },
                onClear = { recipient.value = null },
                onClick = onSearch
            )
        }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ProfileScreen(
                account = account,
                userId = backStackEntry.arguments?.getString("id") ?: "",
                provider = provider,
                viewModelStoreOwner = backStackEntry,
            )
        }
        composable(
            "search/{type}/{query}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("query") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val mode = when (type) {
                "username" -> SearchMode.Username
                "tag" -> SearchMode.Tag
                else -> SearchMode.Default
            }
            SearchScreen(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = provider,
                query = query,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
            )
        }
    }
}
