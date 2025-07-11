package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
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
    content: @Composable () -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    val modifier = if (startDestination == "overlay") {
        Modifier.statusBarsPadding()
            .navigationBarsPadding()
    } else {
        Modifier
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
            Box(modifier) {
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
            Box(modifier) {
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
