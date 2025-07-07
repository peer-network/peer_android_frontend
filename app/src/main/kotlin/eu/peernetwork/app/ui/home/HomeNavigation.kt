package eu.peernetwork.app.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.composer.ComposerScreen
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.messaging.MessagingScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.wallet.WalletScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignNavigation
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun HomeNavigation(
    id: String,
    startDestination: String,
    navController: NavHostController,
    component: Home.Component,
    viewModelStore: ViewModelState,
    onHome: () -> Unit
) {
    DesignNavigation(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeRoute.ROUTES.forEach { route ->
            composable(route.path) {
                when (route) {
                    is HomeRoute.Home -> FeedScreen(
                        id,
                        BuildConfig.PAGING_LIMIT,
                        component,
                        viewModelStore
                    )
                    is HomeRoute.Profile -> ProfileScreen(
                        id,
                        component,
                        viewModelStore,
                    )
                    is HomeRoute.Add -> ComposerScreen(
                        component,
                        viewModelStore,
                        onPostSuccess = onHome
                    )
                    is HomeRoute.Wallet -> WalletScreen(
                        BuildConfig.PAGING_LIMIT,
                        component,
                        viewModelStore
                    )
                    is HomeRoute.Search -> SearchScreen(
                        id,
                        BuildConfig.PAGING_LIMIT,
                        component,
                        viewModelStore,
                    )
                    is HomeRoute.Chat -> MessagingScreen(id, component, viewModelStore.get(id))
                    else -> {}
                }
            }
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
        eu.peernetwork.user.ui.R.string.feed_label,
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
    data object Comment: HomeRoute(
        R.drawable.ic_chat_outline,
        R.drawable.ic_chat,
        R.string.chat_label
    )
    data object Chat : HomeRoute(
        R.drawable.ic_chat_outline,
        R.drawable.ic_chat,
        R.string.chat_label
    )
    companion object {
        val ROUTES = arrayOf(Home, Search, Add, Wallet, Profile, Chat)
        fun get(index: Int): HomeRoute = ROUTES[index]
    }
}
