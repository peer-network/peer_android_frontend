package eu.peernetwork.app.ui.composer

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.blog.ui.explore.ExploreCoverScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.editor.video.VideoScreen
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen
import kotlinx.collections.immutable.toPersistentList

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
            route = "cover?audioUri={audioUri}",
            arguments = listOf(navArgument("audioUri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val audioUriString = backStackEntry.arguments?.getString("audioUri")
            val audioUri = audioUriString?.let { Uri.parse(it) }

            ExploreCoverScreen(
                onImageSelected = { selectedUri ->
                    audioUri?.let { uri ->
                        val updatedFiles = attachment.value.files.map { file ->
                            if (file.uri == uri) file.copy(coverUri = selectedUri)
                            else file
                        }.toPersistentList()

                        attachment.value = UiAttachment.File(
                            attachment.value.media,
                            updatedFiles
                        )
                    }
                    controller.navigate("editor")
                },
                onBack = { controller.navigate("editor") }
            )
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
