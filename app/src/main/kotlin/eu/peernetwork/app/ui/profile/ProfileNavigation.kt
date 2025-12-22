package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import eu.peernetwork.ads.ui.boost.BoostScreen
import eu.peernetwork.ads.ui.dashboard.DashboardScreen
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedExplore
import eu.peernetwork.app.ui.screen.screen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchMode
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.extension.navigate
import eu.peernetwork.user.domain.model.Account

@Composable
fun ProfileNavigation(
    account: Account,
    isModal: Boolean = false,
    controller: NavHostController,
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onCancel: () -> Unit = {},
    content: @Composable (NavBackStackEntry) -> Unit = {},
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "content"
    ) {
        screen(
            route = "content",
            isModal = isModal,
            expanded = true,
            provider = component,
            onCancel = onCancel
        ) { updatedContent(it) }
        screen(
            route = "profile/{id}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
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
            "feed/{tag}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
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
                criteria = tag?.let { Criteria.Content(tag = it) } ?: Criteria.None
            )
        }
        screen(
            route = "settings",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
        ) {
            SettingsScreen(
                account = account,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
        screen(
            route = "search/{type}/{query}",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
            arguments = listOf(
                navArgument("type") { this.type = NavType.StringType },
                navArgument("query") { this.type = NavType.StringType }
            )
        ) { backStackEntry ->
            val searchType = backStackEntry.arguments?.getString("type") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val mode = when (searchType) {
                "username" -> SearchMode.Username
                "tag" -> SearchMode.Tag
                else -> SearchMode.Default
            }
            SearchScreen(
                account = account,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                mode = mode,
                query = query
            )
        }
        screen(
            route = "adverts",
            isModal = isModal,
            provider = component,
            onCancel = onCancel,
        ) { backStackEntry ->
            DashboardScreen(
                id = account.id,
                limit = BuildConfig.PAGING_LIMIT,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                onBack = { controller.popBackStack() }
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
                onFinish = { controller.navigate("profile/${account.id}", backStackEntry) }
            ) { controller.popBackStack() }
        }
    }
}
