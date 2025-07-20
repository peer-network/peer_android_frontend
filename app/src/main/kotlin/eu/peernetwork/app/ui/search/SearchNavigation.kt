package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.window.WindowScreen
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.ui.design.compose.DesignPageWindowMode
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun SearchNavigation(
    userId: String,
    component: Search.Component,
    viewModelStore: ViewModelState,
    controller: NavHostController,
    startDestination: String = "search",
    onCancel: () -> Unit = {},
    content: @Composable (NavHostController) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    var id by remember { mutableStateOf<String>("") }
    val requireUpdate = remember { mutableStateOf(false) }
    val mode = if (startDestination == "overlay") {
        DesignPageWindowMode.DOCKED
    } else {
        DesignPageWindowMode.HIDDEN
    }
    DesignRouter(
        navController = controller,
        startDestination = startDestination,
    ) {
        composable("search") { updatedContent(controller) }
        composable("overlay") { updatedContent(controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            id = backStackEntry.arguments?.getString("id") ?: ""
            WindowScreen(
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = mode,
                onCancel = onCancel,
            ) {
                ProfileScreen(
                    principal = userId,
                    userId = id,
                    provider = component,
                    viewModelStore = viewModelStore,
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
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = mode,
                onCancel = onCancel,
            ) {
                FeedScreen(
                    userId,
                    BuildConfig.PAGING_LIMIT,
                    component,
                    viewModelStore = viewModelStore,
                    title = tag,
                    criteria = tag?.let { Filter.Criteria.Content(tag = it) },
                    requireUpdate = requireUpdate
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
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = mode,
                onCancel = onCancel,
            ) {
                FeedScreen(
                    userId,
                    BuildConfig.PAGING_LIMIT,
                    component,
                    viewModelStore = viewModelStore,
                    title = query,
                    criteria = query?.let { Filter.Criteria.Content(title = it) },
                    requireUpdate = requireUpdate
                )
            }
        }
    }
}

