package eu.peernetwork.app.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun ContentNavigation(
    userId: String,
    postLimit: Int,
    startDestination: String = "content",
    controller: NavHostController,
    component: Content.Component,
    viewModelStore: UiViewModelStore,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = startDestination,
    ) {
        composable("content") { updatedContent() }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ProfileScreen(
                principal = userId,
                userId = backStackEntry.arguments?.getString("id") ?: "",
                provider = component,
                viewModelStore = viewModelStore,
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
            SearchScreen(
                id = userId,
                postLimit = postLimit,
                provider = component,
                viewModelStore = viewModelStore,
                searchState = searchState,
            )
        }
    }
}
