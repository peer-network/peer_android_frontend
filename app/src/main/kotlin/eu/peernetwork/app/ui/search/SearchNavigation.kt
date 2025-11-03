package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedExplorer
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.window.WindowScreen
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.ui.design.material.DesignPageWindowMode
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun SearchNavigation(
    userId: String,
    component: Search.Component,
    viewModelStore: UiViewModelStore,
    controller: NavHostController,
    startDestination: String = "search",
    onCancel: () -> Unit = {},
    content: @Composable (NavBackStackEntry, NavHostController) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    var id by remember { mutableStateOf("") }
    val requireUpdate = remember { mutableStateOf(false) }
    val windowMode = if (startDestination == "overlay") {
        DesignPageWindowMode.DOCKED
    } else {
        DesignPageWindowMode.HIDDEN
    }
    DesignRouter(
        navController = controller,
        startDestination = startDestination,
    ) {
        composable("search") { updatedContent(it, controller) }
        composable("overlay") { updatedContent(it, controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            id = backStackEntry.arguments?.getString("id") ?: ""
            WindowScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = windowMode,
                onCancel = onCancel,
            ) {
                ProfileScreen(
                    principal = userId,
                    userId = id,
                    provider = component,
                    viewModelStoreOwner = backStackEntry,
                )
            }
        }
        composable(
            "feed/{tag}",
            arguments = listOf(navArgument("tag") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            WindowScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = windowMode,
                onCancel = onCancel,
            ) {
                FeedExplorer(
                    id = userId,
                    postLimit = BuildConfig.PAGING_LIMIT,
                    provider = component,
                    viewModelStore = viewModelStore,
                    title = tag,
                    criteria = tag?.let { Filter.Criteria.Content(tag = it) },
                    hasUpdate = requireUpdate
                )
            }
        }
        composable(
            route = "search?query={query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query")
            WindowScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = windowMode,
                onCancel = onCancel,
            ) {
                FeedExplorer(
                    userId,
                    BuildConfig.PAGING_LIMIT,
                    component,
                    viewModelStore = viewModelStore,
                    title = query,
                    criteria = query?.let { Filter.Criteria.Content(title = it) },
                    hasUpdate = requireUpdate
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
                mode = windowMode,
                onCancel = onCancel,
            ) {
                SearchScreen(
                    id = userId,
                    postLimit = BuildConfig.PAGING_LIMIT,
                    provider = component,
                    viewModelStore = viewModelStore,
                    searchState = searchState,
                )
            }
        }
    }
}
