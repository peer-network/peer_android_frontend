package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.member.MemberScreen
import eu.peernetwork.user.ui.settings.SettingsScreen
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun ProfileScreen(
    userId: String,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    title: String? = null,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    val coroutine = rememberCoroutineScope()
    var id by remember { mutableStateOf<String>("") }
    DesignRouter(navController = navController, startDestination = "profile/$userId") {
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val photoState = rememberLazyListState()
            val videoState = rememberLazyListState()
            id = backStackEntry.arguments?.getString("id") ?: ""
            MemberScreen(
                id = id,
                limit = BuildConfig.PAGING_LIMIT,
                onSettings = { navController.navigateIfNecessary("settings") },
                provider = component,
                viewModelStoreOwner = viewModelStore.get(id),
                photoState = photoState,
                videoState = videoState,
                onHashtagClick = { navController.navigateToTagSearch(it) },
                onMentionClick = { navController.navigateToUsernameSearch(it) },
                imageOnClick = { navController.navigateIfNecessary("profile/$it") },
            )
            DesignTitleBarHost("ProfileScreen$id", {
                coroutine.launch {
                    photoState.animateScrollToItem(0)
                    videoState.animateScrollToItem(0)
                }
            }) {
                titleBar {
                    DesignTitle {
                        Text(title ?: stringResource(R.string.profile_label))
                    }
                }
            }
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
