package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.social.ui.renderder.UserRenderer

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
    }
}
