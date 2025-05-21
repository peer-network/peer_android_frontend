package eu.peernetwork.app.ui.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen

@Composable
fun ComposerNavigation(
    attachment: MutableState<UiAttachment>,
    controller: NavHostController,
    provider: UiComponentProvider,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "gallery"
    ) {
        composable("gallery") { updatedContent() }
        composable("explorer") {
            ExplorerScreen(
                attachment = attachment,
                onFinish = { controller.popBackStack() },
                provider = provider,
            )
        }
    }
}
