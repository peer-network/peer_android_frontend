package eu.peernetwork.app.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignNavigation
import eu.peernetwork.core.ui.extension.attachIfNecessary

@Composable
fun HomeNavigation(
    state: MutableState<Int>,
    navController: NavHostController,
    onNavigate: (Int) -> Unit,
    content: @Composable (HomeRoute) -> Unit
) {
    val startDestination = remember { HomeRoute.get(state.value).path }
    DesignNavigation(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeRoute.ROUTES.forEach { route ->
            composable(route.path) { content(route) }
        }
    }
    LaunchedEffect(state.value) {
        onNavigate(state.value)
        navController.attachIfNecessary(HomeRoute.get(state.value).path)
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
        R.string.home_label,
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
    companion object {
        val ROUTES = arrayOf(Home, Search, Add, Wallet, Profile)
        fun get(index: Int): HomeRoute = ROUTES[index]
    }
}
