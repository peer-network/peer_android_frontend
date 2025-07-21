package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.window.WindowScreen
import eu.peernetwork.core.ui.design.compose.DesignPageWindowMode
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState
import java.net.URLEncoder

@Composable
fun FeedNavigation(
    userId: String,
    postLimit: Int,
    startDestination: String = "content",
    controller: NavHostController,
    component: Feed.Component,
    viewModelStore: ViewModelState,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    val mode = if (startDestination == "overlay") {
        DesignPageWindowMode.DOCKED
    } else {
        DesignPageWindowMode.HIDDEN
    }
    DesignRouter(
        navController = controller,
        startDestination = startDestination,
    ) {
        composable("content") { updatedContent() }
        composable("overlay") { updatedContent() }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            WindowScreen(
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = mode,
                onCancel = onCancel,
            ) {
                ProfileScreen(
                    principal = userId,
                    userId = backStackEntry.arguments?.getString("id") ?: "",
                    provider = component,
                    viewModelStore = viewModelStore,
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
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = mode,
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

fun NavHostController.navigateToTagSearch(tag: String) {
    val cleanTag = tag.removePrefix("#")
    val encoded = URLEncoder.encode(cleanTag, "UTF-8")
    navigate("search/tag/$encoded") {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToUsernameSearch(username: String) {
    val cleanUsername = username.removePrefix("@")
    val encoded = URLEncoder.encode(cleanUsername, "UTF-8")
    navigate("search/username/$encoded") {
        launchSingleTop = true
    }
}
