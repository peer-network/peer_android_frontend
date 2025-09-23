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
import eu.peernetwork.app.ui.feed.FeedExplorer
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.wallet.WalletScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignNavigation
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun HomeNavigation(
    id: String,
    startDestination: String,
    navController: NavHostController,
    component: Home.Component,
    viewModelStore: UiViewModelStore,
    onExplore: () -> Unit,
    onHome: () -> Unit
) {
    val hasUpdate = remember { mutableStateOf(false) }
    val handleOnHomeClick by rememberUpdatedState(onHome)
    DesignNavigation(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeRoute.ROUTES.forEach { route ->
            composable(route.path) {
                when (route) {
                    is HomeRoute.Home -> FeedScreen(
                        id = id,
                        postLimit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStore = viewModelStore,
                        hasUpdate = hasUpdate,
                        onExplore = onExplore
                    )
                    is HomeRoute.Profile -> ProfileScreen(
                        principal = id,
                        userId = id,
                        provider = component,
                        viewModelStore = viewModelStore,
                    )
                    is HomeRoute.Add -> ComposerScreen(
                        provider = component,
                        viewModelStore = viewModelStore,
                        onPostSuccess = {
                            hasUpdate.value = true
                            handleOnHomeClick()
                        }
                    )
                    is HomeRoute.Wallet -> WalletScreen(
                        id = id,
                        postLimit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelState = viewModelStore
                    )
                    is HomeRoute.Search -> SearchScreen(
                        id = id,
                        postLimit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStore = viewModelStore,
                    )
                    else -> {}
                }
            }
        }
        composable(HomeRoute.Explore.path) {
            FeedExplorer(
                id = id,
                postLimit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStore = viewModelStore,
                hasUpdate = hasUpdate
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