package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import eu.peernetwork.ads.ui.boost.BoostScreen
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.screen.screen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.extension.navigate
import eu.peernetwork.user.domain.model.Account

@Composable
fun FeedNavigation(
    account: Account,
    isModal: Boolean = false,
    controller: NavHostController,
    component: Feed.Component,
    onCancel: () -> Unit = {},
    content: @Composable (NavBackStackEntry) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "content",
    ) {
        screen(
            route = "content",
            isModal = isModal,
            expanded = true,
            provider = component,
            onCancel = onCancel
        ) { updatedContent(it) }
        screen(
            "profile/{id}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
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
                onProfile = { controller.navigate("profile/${account.id}", backStackEntry) },
                onFinish = { controller.navigate("feed", backStackEntry) }
            ) { controller.popBackStack() }
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
            val mode = when (type) {
                "username" -> SearchMode.Username
                "tag" -> SearchMode.Tag
                else -> SearchMode.Default
            }
            SearchScreen(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                query = query,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
            )
        }
    }
}
