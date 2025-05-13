package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.social.ui.renderder.UserRenderer

@Composable
fun SearchNavigation(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    component: Search.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    search: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    DesignRouter(
        navController = controller,
        startDestination = "search",
    ) {
        composable("search") { search(controller) }
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
            "feed/{tag}",
            arguments = listOf(navArgument("tag") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            FeedScreen(
                id,
                title,
                BuildConfig.PAGING_LIMIT,
                component,
                viewModelStoreOwner = UiViewModel.Owner(),
                criteria = tag?.let { Filter.Criteria.Content(tag = it) }
            )
        }
        composable(
            "search/{title}",
            arguments = listOf(navArgument("title") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("title")
            FeedScreen(
                id,
                title,
                BuildConfig.PAGING_LIMIT,
                component,
                viewModelStoreOwner = UiViewModel.Owner(),
                criteria = query?.let { Filter.Criteria.Content(title = it) }
            )
        }
    }
}
