package eu.peernetwork.app.ui.profile

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
import eu.peernetwork.app.ui.window.WindowScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignPageWindowMode
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun ProfileNavigation(
    principal: String,
    userId: String,
    startDestination: String = "content",
    controller: NavHostController,
    provider: UiComponentProvider,
    component: Profile.Component,
    viewModelStore: UiViewModelStore,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val updatedContent by rememberUpdatedState(content)
    val windowMode = if (startDestination == "overlay") {
        DesignPageWindowMode.DOCKED
    } else {
        DesignPageWindowMode.HIDDEN
    }
    DesignRouter(
        navController = controller,
        startDestination = startDestination
    ) {
        composable("content") { updatedContent() }
        composable("overlay") { updatedContent() }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            WindowScreen(
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = windowMode,
                onCancel = onCancel,
            ) {
                ProfileScreen(
                    principal = principal,
                    userId = id,
                    provider = provider,
                    viewModelStore = viewModelStore,
                )
            }
        }
        composable("settings") {
            WindowScreen(
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = windowMode,
                onCancel = onCancel,
            ) { SettingsScreen(userId, component, viewModelStore) }
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
            WindowScreen(
                id = userId,
                provider = component,
                viewModelStore = viewModelStore,
                mode = windowMode,
                onCancel = onCancel,
            ) {
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
