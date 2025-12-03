package eu.peernetwork.app.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.user.domain.model.Account

@Composable
fun ContentNavigation(
    account: Account,
    postLimit: Int,
    overlay: MutableState<String?>,
    startDestination: String = "content",
    controller: NavHostController,
    component: Content.Component,
    viewModelStore: UiViewModelStore,
    onCancel: () -> Unit = {},
    content: @Composable (NavBackStackEntry) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = startDestination,
    ) {
        composable("content") { updatedContent(it) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ProfileScreen(
                account = account,
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
                account = account,
                query = query,
                limit = postLimit,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
            )
        }
    }
}
