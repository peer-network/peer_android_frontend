package eu.peernetwork.app.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.user.domain.model.Account

@Composable
fun HomeNavigation(
    account: Account,
    startDestination: String,
    navController: NavHostController,
    component: Home.Component,
    viewModelStore: UiViewModelStore,
    onExplore: () -> Unit,
    onHome: () -> Unit
) {
    val refresh = remember { mutableStateOf(false) }
    val handleOnHomeClick by rememberUpdatedState(onHome)
    DesignNavigation(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeRoute.ROUTES.forEach { route ->
            composable(route.path) { backStackEntry ->
                when (route) {
                    is HomeRoute.Home -> FeedScreen(
                        account = account,
                        limit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStoreOwner = backStackEntry,
                        refresh = refresh,
                        onExplore = onExplore
                    )
                    is HomeRoute.Profile -> ProfileScreen(
                        account = account,
                        userId = account.id,
                        provider = component,
                        viewModelStoreOwner = backStackEntry,
                    )
                    is HomeRoute.Add -> ComposerScreen(
                        provider = component,
                        viewModelStoreOwner = viewModelStore.get(account.id),
                        onPostSuccess = {
                            refresh.value = true
                            handleOnHomeClick()
                        }
                    )
                    is HomeRoute.Wallet -> WalletScreen(
                        account = account,
                        postLimit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelState = viewModelStore
                    )
                    is HomeRoute.Search -> SearchScreen(
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
        composable(HomeRoute.Explore.path) { backStackEntry ->
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
                viewModelStore = viewModelStore
            )
        }
    }
}

sealed class HomeRoute(
    val icon: Int,
    val activeIcon: Int,
    val label: Int,
) {
    val path: String = this::class.java.simpleName
    data object Home: HomeRoute(
        R.drawable.ic_home_outline,
        R.drawable.ic_home,
        eu.peernetwork.blog.ui.R.string.feed_label,
    )
    data object Search: HomeRoute(
        R.drawable.ic_search_outline,
        R.drawable.ic_search,
        R.string.search_label
    )
    data object Add: HomeRoute(
        R.drawable.ic_add_outline,
        R.drawable.ic_add,
        R.string.add_label
    )
    data object Wallet: HomeRoute(
        R.drawable.ic_wallet_outline,
        R.drawable.ic_wallet,
        R.string.wallet_label
    )
    data object Profile: HomeRoute(
        R.drawable.ic_profile_outline,
        R.drawable.ic_profile,
        R.string.profile_label
    )
    data object Explore: HomeRoute(
        R.drawable.ic_trend_outline,
        R.drawable.ic_trend,
        R.string.trend_label
    )
    companion object {
        val ROUTES = arrayOf(Home, Search, Add, Wallet, Profile)
        fun get(index: Int): HomeRoute = ROUTES[index]
    }
}