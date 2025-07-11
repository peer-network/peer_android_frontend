package eu.peernetwork.app.ui.profile

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
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.app.ui.search.SearchState
import eu.peernetwork.app.ui.settings.SettingsScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun ProfileNavigation(
    principal: String,
    userId: String,
    startDestination: String = "content",
    controller: NavHostController,
    provider: UiComponentProvider,
    component: Profile.Component,
    viewModelStore: ViewModelState,
    content: @Composable () -> Unit = {},
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
        startDestination = startDestination
    ) {
        composable("overlay") { updatedContent() }
        composable("content") { updatedContent() }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            Box(modifier) {
                ProfileScreen(
                    principal = principal,
                    userId = id,
                    provider = provider,
                    viewModelStore = viewModelStore,
                )
            }
        }
        composable("settings") {
            Box(modifier) { SettingsScreen(userId, component, viewModelStore) }
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
            Box(modifier) {
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
}
