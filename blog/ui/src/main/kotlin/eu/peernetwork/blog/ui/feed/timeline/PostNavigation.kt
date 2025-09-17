package eu.peernetwork.blog.ui.feed.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.design.compose.DesignNavigation

@Composable
fun PostNavigation(
    explore: @Composable (NavHostController) -> Unit,
    content: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedExplore by rememberUpdatedState(explore)
    val updatedContent by rememberUpdatedState(content)
    DesignNavigation(
        navController = controller,
        startDestination = "timeline"
    ) {
        composable("timeline") {
            updatedContent(controller)
        }
        composable("explore") {
            updatedExplore(controller)
        }
    }
}