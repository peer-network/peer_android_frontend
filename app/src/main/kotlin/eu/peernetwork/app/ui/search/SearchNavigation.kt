package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedExplore
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.user.domain.model.Account

@Composable
fun SearchNavigation(
    account: Account,
    component: Search.Component,
    controller: NavHostController,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val requireUpdate = remember { mutableStateOf(false) }
    DesignRouter(
        navController = controller,
        startDestination = "search",
    ) {
        composable("search") { updatedContent() }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("id") ?: ""
            ProfileScreen(
                account = account,
                userId = uuid,
                provider = component,
                viewModelStoreOwner = backStackEntry,
            )
        }
        composable(
            "feed/{tag}",
            arguments = listOf(navArgument("tag") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            FeedExplore(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                title = tag,
                criteria = tag?.let { Criteria.Content(tag = it) } ?: Criteria.None,
                refresh = requireUpdate
            )
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
            FeedExplore(
                account = account,
                BuildConfig.PAGING_LIMIT,
                component,
                viewModelStoreOwner = backStackEntry,
                title = query,
                criteria = query?.let { Criteria.Content(title = it) } ?: Criteria.None,
                refresh = requireUpdate
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
            SearchScreen(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                query = query,
                mode = when (type) {
                    "username" -> SearchMode.Username
                    "tag" -> SearchMode.Tag
                    else -> SearchMode.Default
                },
            )
        }
    }
}
