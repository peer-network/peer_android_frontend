package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import eu.peernetwork.ads.ui.boost.BoostScreen
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.screen.screen
import eu.peernetwork.app.ui.feed.FeedExplore
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.extension.navigate
import eu.peernetwork.user.domain.model.Account

@Composable
fun SearchNavigation(
    account: Account,
    isModal: Boolean = false,
    component: Search.Component,
    controller: NavHostController,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "search",
    ) {
        screen(
            route = "search",
            isModal = isModal,
            expanded = true,
            provider = component,
            onCancel = onCancel,
        ) { updatedContent() }
        screen(
            route = "profile/{id}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProfileScreen(
                account = account,
                userId = id,
                provider = component,
                viewModelStoreOwner = backStackEntry,
            )
        }
        screen(
            route = "feed",
            isModal = isModal,
            provider = component,
            onCancel = onCancel
        ) { backStackEntry ->
            FeedExplore(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                title = null,
                criteria = Criteria.None
            )
        }
        screen(
            route = "boost/{id}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            BoostScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                onProfile = {
                    controller.navigate("profile/${account.id}") {
                        popUpTo(backStackEntry.destination.id) {
                            inclusive = true
                        }
                    }
                },
                onFinish = { controller.navigate("feed", backStackEntry) }
            ) { controller.popBackStack() }
        }
        screen(
            route = "search?query={query}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
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
                criteria = query?.let { Criteria.Content(title = it) } ?: Criteria.None
            )
        }
        screen(
            "search/{type}/{query}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
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
