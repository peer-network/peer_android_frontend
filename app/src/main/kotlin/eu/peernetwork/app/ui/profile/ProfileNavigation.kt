package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun ProfileNavigation(
    userId: String,
    title: String?,
    limit: Int,
    startDestination: String? = null,
    controller: NavHostController,
    component: Profile.Component,
    viewModelStore: ViewModelState,
    onPhotoClick: (String, Int) -> Unit = { id, position -> },
    onVideoClick: (String, Int) -> Unit = { id, position -> },
    content: @Composable (NavHostController) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = startDestination ?: "profile/$userId"
    ) {
        composable("overlay") { updatedContent(controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val photoState = rememberLazyListState()
            val videoState = rememberLazyListState()
            ProfilePreview(
                id = id,
                title = title,
                limit = limit,
                onSettings = { controller.navigateIfNecessary("settings") },
                component = component,
                viewModelStoreOwner = viewModelStore.get(id),
                photoState = photoState,
                videoState = videoState,
                onPhotoClick = onPhotoClick,
                onVideoClick = onVideoClick,
                onHashtagClick = { controller.navigateToTagSearch(it) },
                onMentionClick = { controller.navigateToUsernameSearch(it) },
                onAuthorClicked = { controller.navigateIfNecessary("profile/$it") },
            )
        }
        composable("settings") { SettingsScreen(component, viewModelStore.get(userId)) }
        composable(
            route = "search/{type}/{query}",
            arguments = listOf(
                navArgument("type") { this.type = NavType.StringType },
                navArgument("query") { this.type = NavType.StringType }
            )
        ) { backStackEntry ->
            val searchType = backStackEntry.arguments?.getString("type") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val searchState = when (searchType) {
                "username" -> SearchState.Active.Username(query)
                "tag" -> SearchState.Active.Tag(query)
                else -> SearchState.Default
            }
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
