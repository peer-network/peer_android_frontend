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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun SearchNavigation(
    userId: String,
    component: Search.Component,
    viewModelStore: ViewModelState,
    search: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedContent by rememberUpdatedState(search)
    var id by remember { mutableStateOf<String>("") }
    DesignRouter(
        navController = controller,
        startDestination = "search",
    ) {
        composable("search") { updatedContent(controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            id = backStackEntry.arguments?.getString("id") ?: ""
            ProfileScreen(
                userId = id,
                provider = component,
                viewModelStore = viewModelStore,
            )
        }
        composable(
            "feed/{tag}",
            arguments = listOf(navArgument("tag") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            FeedScreen(
                userId,
                BuildConfig.PAGING_LIMIT,
                component,
                viewModelStore = viewModelStore,
                title = tag,
                criteria = tag?.let { Filter.Criteria.Content(tag = it) }
            )
        }
        composable(
            "search/{title}",
            arguments = listOf(navArgument("title") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("title")
            FeedScreen(
                userId,
                BuildConfig.PAGING_LIMIT,
                component,
                viewModelStore = viewModelStore,
                title = query,
                criteria = query?.let { Filter.Criteria.Content(title = it) }
            )
        }
    }
}
