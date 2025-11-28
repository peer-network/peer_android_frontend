package eu.peernetwork.blog.ui.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.interaction.user.UserList
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun CommentNavigation(
    limit: Int,
    controller: NavHostController,
    component: Comment.Component,
    content: @Composable () -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "content"
    ) {
        composable(route = "content") { updatedContent() }
        composable(
            route = "likes/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            UserList(
                id = id,
                limit = limit,
                engagement = Engagement.Content.LikedComment,
                provider = component,
                viewModelStoreOwner = backStackEntry,
            )
        }
    }
}
