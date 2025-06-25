package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun ProfileNavigation(
    userId: String,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    profile: @Composable (String, NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedProfile by rememberUpdatedState(profile)
    var id by remember { mutableStateOf<String>("") }
    DesignRouter(navController = controller, startDestination = "profile/$userId") {
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            id = backStackEntry.arguments?.getString("id") ?: ""
            updatedProfile(id, controller)
        }
        composable("settings") { SettingsScreen(userId, provider, viewModelStore) }
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
                provider = provider,
                viewModelStore = viewModelStore,
                searchState = searchState,
            )
        }
    }
}
