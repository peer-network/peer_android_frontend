package eu.peernetwork.app.ui.composer

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen
import eu.peernetwork.media.ui.editor.video.VideoScreen
import kotlinx.collections.immutable.toPersistentList

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
        startDestination = "editor"
    ) {
        composable("editor") { updatedContent() }
        composable("explorer") {
            ExplorerScreen(
                attachment = attachment,
                onFinish = { controller.popBackStack() },
                provider = provider,
            )
        }
        composable(
            "videoEditor/{path}",
            arguments = listOf(navArgument("path") { type = NavType.StringType })
        ) { backStackEntry ->

            val rawPath = backStackEntry.arguments!!.getString("path")!!
            val absPath = Uri.decode(rawPath)

            VideoScreen(
                url = absPath,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
                onVideoTrimmed = { trimmedFile ->
                    val current = attachment.value
                    val updated = current.files.map { file ->
                        if (file.uri.toString() == absPath)
                            file.copy(uri = trimmedFile.toUri())
                        else file
                    }
                    attachment.value = UiAttachment.File(
                        current.media,
                        updated.toPersistentList()
                    )
                    controller.popBackStack()
                },
                onCancel = { controller.popBackStack() }
            )
        }
    }
}
