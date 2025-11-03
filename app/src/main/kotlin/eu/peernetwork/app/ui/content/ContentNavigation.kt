package eu.peernetwork.app.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.window.WindowScreen
import eu.peernetwork.core.ui.design.material.DesignPageWindowMode
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun ContentNavigation(
    userId: String,
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
    val windowMode = remember { derivedStateOf {
        if (overlay.value != null) {
            DesignPageWindowMode.DOCKED
        } else {
            DesignPageWindowMode.HIDDEN
        }
    } }
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
            WindowScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = windowMode.value,
                onCancel = onCancel,
            ) {
                ProfileScreen(
                    principal = userId,
                    userId = backStackEntry.arguments?.getString("id") ?: "",
                    provider = component,
                    viewModelStoreOwner = backStackEntry,
                )
            }
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
            WindowScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = windowMode.value,
                onCancel = onCancel,
            ) {
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
}
