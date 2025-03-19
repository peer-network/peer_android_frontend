package eu.peernetwork.app.ui.home

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.compose.DesignOption
import eu.peernetwork.core.ui.extension.attachIfNecessary

@Composable
fun HomeNavigation(
    state: MutableState<Int>,
    navController: NavHostController,
    content: @Composable (HomeRoute) -> Unit
) {
    val startDestination = remember { HomeRoute.get(state.value).path }
    val navState by remember { derivedStateOf { HomeRoute.get(state.value) } }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        HomeRoute.ROUTES.forEach { route ->
            composable(route.path) { content(route) }
        }
    }
    LaunchedEffect(navState) {
        if (navState.path != startDestination) {
            navController.attachIfNecessary(navState.path)
        }
    }
}

@Composable
fun RowScope.HomeOptions() {
    DesignOption(
        text = "2",
        painter = painterResource(id = R.drawable.ic_chat),
        contentDescription = "Action Icon",
        onClick = {  }
    )
    DesignOption(
        text = "1",
        painter = painterResource(id = R.drawable.ic_dislike),
        contentDescription = "Action Icon",
        onClick = {  }
    )
    DesignOption(
        text = "3",
        painter = painterResource(id = R.drawable.ic_like),
        contentDescription = "Action Icon",
        onClick = {  }
    )
}

sealed class HomeRoute(
    val icon: Int,
    val activeIcon: Int,
    val label: Int,
    val hasOptions: Boolean = false,
) {
    val path: String = this::class.java.simpleName
    data object Home: HomeRoute(
        R.drawable.ic_home_outline,
        R.drawable.ic_home,
        eu.peernetwork.app.R.string.home_label,
        true
    )
    data object Search: HomeRoute(
        R.drawable.ic_search_outline,
        R.drawable.ic_search,
        eu.peernetwork.app.R.string.search_label
    )
    data object Add: HomeRoute(
        R.drawable.ic_add_outline,
        R.drawable.ic_add,
        eu.peernetwork.app.R.string.add_label
    )
    data object Wallet: HomeRoute(
        R.drawable.ic_wallet_outline,
        R.drawable.ic_wallet,
        eu.peernetwork.app.R.string.wallet_label
    )
    data object Profile: HomeRoute(
        R.drawable.ic_profile_outline,
        R.drawable.ic_profile,
        eu.peernetwork.app.R.string.profile_label
    )
    data object Comment: HomeRoute(
        R.drawable.ic_chat_outline,
        R.drawable.ic_chat,
        eu.peernetwork.app.R.string.chat_label
    )
    companion object {
        val ROUTES = arrayOf(Home, Search, Add, Wallet, Profile)
        fun get(index: Int): HomeRoute = ROUTES[index]
    }
}
