package eu.peernetwork.app.ui.profile.v2

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
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun ProfileNavigation(
    principal: String,
    userId: String,
    controller: NavHostController,
    provider: UiComponentProvider,
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
                principal = principal,
                userId = id,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
            )
        }
        composable("settings") {
            SettingsScreen(
                userId = userId,
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
            val searchState = when (searchType) {
                "username" -> SearchState.Active.Username(query)
                "tag" -> SearchState.Active.Tag(query)
                else -> SearchState.Default
            }
            SearchScreen(
                id = userId,
                postLimit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStore = UiViewModelStore.Delegate(),
                searchState = searchState,
            )
        }
        composable("adverts") { backStackEntry ->
            DashboardScreen(
                id = principal,
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
