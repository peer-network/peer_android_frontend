package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.member.MemberScreen
import eu.peernetwork.social.ui.renderder.UserRenderer
import eu.peernetwork.user.ui.settings.SettingsScreen
import java.net.URLEncoder

@Composable
fun ProfileScreen(
    userId: String,
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    type: UserRenderer.Type = UserRenderer.Type.ACCOUNT,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }

    DesignRouter(navController = navController, startDestination = "profile") {
        composable("profile") {
            MemberScreen(
                id = userId,
                limit = BuildConfig.PAGING_LIMIT,
                type = type,
                onSettings = { navController.navigateIfNecessary("settings") },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onHashtagClick = { tag -> navController.navigateToTagSearch(tag) },
                onMentionClick = { username -> navController.navigateToUsernameSearch(username) },
                imageOnClick = { image -> navController.navigateIfNecessary("member/$image") }
            )
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.profile_label)
            }
        }
        composable(
            "member/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            MemberScreen(
                id = id,
                limit = BuildConfig.PAGING_LIMIT,
                type = if (id == userId) UserRenderer.Type.ACCOUNT else UserRenderer.Type.USER,
                onSettings = { navController.navigateIfNecessary("settings") },
                provider = component,
                viewModelStoreOwner = if (id == userId) viewModelStoreOwner else UiViewModel.Owner(),
                onHashtagClick = { tag -> navController.navigateToTagSearch(tag) },
                onMentionClick = { username -> navController.navigateToUsernameSearch(username) },
                imageOnClick = { image -> navController.navigateIfNecessary("member/$image") }
            )
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.profile_label)
            }
        }
        composable("settings") {
            SettingsScreen(component, viewModelStoreOwner)
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.settings_label)
            }
        }
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
                title = title,
                postLimit = BuildConfig.PAGING_LIMIT,
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
