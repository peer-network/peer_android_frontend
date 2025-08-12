package eu.peernetwork.app.ui.composer

import android.net.Uri
<<<<<<< HEAD
import android.os.Bundle
=======
>>>>>>> d0719d61 (Audio cover upload)
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.blog.ui.explore.ExploreCoverScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiOffset
import eu.peernetwork.media.ui.editor.video.VideoScreen
import eu.peernetwork.media.ui.selector.explorer.ExplorerScreen
<<<<<<< HEAD
import kotlinx.collections.immutable.persistentListOf
import java.io.File
=======
import kotlinx.collections.immutable.toPersistentList
>>>>>>> d0719d61 (Audio cover upload)

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
                if (it.media is UiMimeType.Video) {
                    val path = it.files.first().path
                    controller.navigateIfNecessary("video?path=$path")
                } else {
                    attachment.value = it
                    controller.route("editor")
                }
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
                            if (file.uri == uri) file.copy(cover = selectedUri)
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
            VideoScreen(
                path = path,
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner,
                onDiscard = { controller.popBackStack() }
            ) { start, stop, duration ->
                val props = Bundle()
                if (stop - start != duration) {
                    props.putParcelable(path, UiOffset.Value(start, stop))
                }
                attachment.value = UiAttachment.File(
                    type = UiMimeType.Video,
                    uris = persistentListOf(UiFile(Uri.fromFile(File(path)), path, props))
                )
                controller.route("editor")
            }
        }
    }
}
