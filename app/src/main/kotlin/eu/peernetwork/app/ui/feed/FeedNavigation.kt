package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun FeedNavigation(
    id: String,
    controller: NavHostController,
    component: Feed.Component,
    content: @Composable (NavBackStackEntry) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "content",
    ) {
        composable("content") { updatedContent(it) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ProfileScreen(
                principal = id,
                userId = backStackEntry.arguments?.getString("id") ?: "",
                provider = component,
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
                id = id,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                query = query,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
            )
        }
    }
}
