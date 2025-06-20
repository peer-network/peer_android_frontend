package eu.peernetwork.app.ui.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.editor.video.VideoScreen
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen

@Composable
fun ComposerNavigation(
    attachment: MutableState<UiAttachment>,
    controller: NavHostController,
    provider: UiComponentProvider,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val viewModelStore = remember { ViewModelState() }  // share ViewModelStore here

    DesignRouter(
        navController = controller,
        startDestination = "editor"
    ) {
        composable("editor") { updatedContent() }
        composable("video") { backStackEntry ->
            val type = UiMimeType.Video
            val directory = remember { mutableStateOf<String?>(null) }

            VideoScreen(
                type = type,
                directory = directory,
                attachment = attachment,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
                isEdit = true
            )
        }

        composable("explorer") {
            ExplorerScreen(
                attachment = attachment,
                onEdit = { controller.navigateIfNecessary("video") },
                onFinish = { controller.popBackStack() },
                provider = provider,
            )
        }
    }
}
