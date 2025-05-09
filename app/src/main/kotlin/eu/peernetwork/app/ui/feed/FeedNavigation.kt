package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.social.ui.renderder.UserRenderer
import java.net.URLEncoder

@Composable
fun FeedNavigation(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    component: Feed.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    feed: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    DesignRouter(
        navController = controller,
        startDestination = "feed",
    ) {
        composable("feed") { feed(controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            ProfileScreen(
                userId = userId ?: id,
                title = title,
                type = if (userId == id) {
                    UserRenderer.Type.ACCOUNT
                } else {
                    userId?.let {
                        UserRenderer.Type.USER
                    } ?: UserRenderer.Type.ACCOUNT
                },
                provider = component,
                viewModelStoreOwner = if (userId == id) {
                    viewModelStoreOwner
                } else {
                    UiViewModel.Owner()
                }
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
            val searchState = when (type) {
                "username" -> SearchState.Username(query)
                "tag" -> SearchState.Tag(query)
                else -> SearchState.Default
            }
            SearchScreen(
                id = id,
                title = title,
                postLimit = 20,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                searchState = searchState
            )
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