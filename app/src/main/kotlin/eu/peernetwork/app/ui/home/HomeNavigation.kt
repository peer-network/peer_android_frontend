package eu.peernetwork.app.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.composer.ComposerScreen
import eu.peernetwork.app.ui.content.ContentScreen
import eu.peernetwork.app.ui.feed.FeedExplore
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.wallet.WalletScreen
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.user.domain.model.Account

@Composable
fun HomeNavigation(
    account: Account,
    startDestination: String,
    navController: NavHostController,
    component: Home.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onExplore: () -> Unit,
    onHome: () -> Unit
) {
    val refresh = remember { mutableStateOf(false) }
    val handleOnHomeClick by rememberUpdatedState(onHome)
    DesignNavigation(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeMenu.MENU.forEach { route ->
            composable(route.path) { backStackEntry ->
                when (route) {
                    is HomeMenu.Home -> FeedScreen(
                        account = account,
                        limit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStoreOwner = backStackEntry,
                        refresh = refresh,
                        onExplore = onExplore
                    )
                    is HomeMenu.Profile -> ProfileScreen(
                        account = account,
                        userId = account.id,
                        provider = component,
                        viewModelStoreOwner = backStackEntry,
                    )
                    is HomeMenu.Add -> ComposerScreen(
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner,
                        onPostSuccess = {
                            refresh.value = true
                            handleOnHomeClick()
                        }
                    )
                    is HomeMenu.Wallet -> WalletScreen(
                        account = account,
                        postLimit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner
                    )
                    is HomeMenu.Search -> SearchScreen(
                        account = account,
                        limit = BuildConfig.PAGING_LIMIT,
                        mode = SearchMode.Default,
                        provider = component,
                        viewModelStoreOwner = backStackEntry
                    )
                    else -> {}
                }
            }
        }
        composable(HomeMenu.Explore.path) { backStackEntry ->
            FeedExplore(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                refresh = refresh
            )
        }
        composable("post/{id}") {
            val postId = it.arguments?.getString("id") ?: ""
            ContentScreen(
                account = account,
                postId = postId,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
    }
}
