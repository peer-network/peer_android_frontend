package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun FeedNavigation(
    id: String,
    controller: NavHostController,
    component: Feed.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
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
            val searchState = when (type) {
                "username" -> SearchState.Active.Username(query)
                "tag" -> SearchState.Active.Tag(query)
                else -> SearchState.Default
            }
//            SearchScreen(
//                id = id,
//                postLimit = postLimit,
//                provider = component,
//                viewModelStore = UiViewModel.Owner(),
//                searchState = searchState,
//            )
        }
    }
}
