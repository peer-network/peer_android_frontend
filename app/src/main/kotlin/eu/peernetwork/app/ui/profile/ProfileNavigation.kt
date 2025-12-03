package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.ads.ui.boost.BoostScreen
import eu.peernetwork.ads.ui.dashboard.DashboardScreen
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.user.domain.model.Account

@Composable
fun ProfileNavigation(
    account: Account,
    controller: NavHostController,
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (NavBackStackEntry) -> Unit = {},
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "content"
    ) {
        composable("content") { updatedContent(it) }
        composable(
            route = "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProfileScreen(
                account = account,
                userId = id,
                provider = component,
                viewModelStoreOwner = backStackEntry,
            )
        }
        composable("settings") {
            SettingsScreen(
                account = account,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
        composable(
            route = "search/{type}/{query}",
            arguments = listOf(
                navArgument("type") { this.type = NavType.StringType },
                navArgument("query") { this.type = NavType.StringType }
            )
        ) { backStackEntry ->
            val searchType = backStackEntry.arguments?.getString("type") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val mode = when (searchType) {
                "username" -> SearchMode.Username
                "tag" -> SearchMode.Tag
                else -> SearchMode.Default
            }
            SearchScreen(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
                query = query
            )
        }
        composable("adverts") { backStackEntry ->
            DashboardScreen(
                id = account.id,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                onBack = { controller.popBackStack() }
            ) {}
        }
        composable("boost/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            BoostScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = backStackEntry
            ) { controller.popBackStack() }
        }
    }
}
