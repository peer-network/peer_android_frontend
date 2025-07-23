package eu.peernetwork.app.ui.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.editor.video.VideoScreen
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen

@Composable
fun ComposerNavigation(
    attachment: MutableState<UiAttachment>,
    controller: NavHostController,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "editor"
    ) {
        composable("editor") { updatedContent() }
        composable("explorer") {
            ExplorerScreen(
                attachment = attachment,
                provider = provider,
            ) {
                attachment.value = it
                controller.route("editor")
            }
        }
        composable(
            route = "video?path={path}",
            arguments = listOf(navArgument("path") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""
            VideoScreen(path, provider, viewModelStoreOwner)
        }
    }
}
