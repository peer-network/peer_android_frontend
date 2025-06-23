package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.compose.ContentOverlay
import eu.peernetwork.blog.ui.post.photo.PhotoOverlay
import eu.peernetwork.blog.ui.post.video.VideoOverlay
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.model.ViewModelState

sealed interface ProfileOverlayState {
    data object Empty : ProfileOverlayState

    data class Photo(
        val id: String,
        val position: Int
    ) : ProfileOverlayState

    data class Video(
        val id: String,
        val position: Int
    ) : ProfileOverlayState
}

@Composable
fun ProfileOverlay(
    overlay: MutableState<ProfileOverlayState>,
    userId: String,
    title: String?,
    limit: Int,
    component: Profile.Component,
    viewModelStore: ViewModelState,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val key = remember { System.currentTimeMillis().toString() }
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is ProfileOverlayState.Empty) }
    updatedContent()
    DesignDialogSheet(
        key,
        visible,
        onAnimationComplete = {
            if (!it) {
                component.videoInteractor().restore()
                overlay.value = ProfileOverlayState.Empty
            }
        }
    ) {
        ContentOverlay(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val controller = rememberNavController()
            ProfileNavigation(
                userId = userId,
                title = title,
                limit = limit,
                startDestination = "overlay",
                controller = controller,
                component = component,
                viewModelStore = viewModelStore,
            ) { controller ->
                when (overlay.value) {
                    is ProfileOverlayState.Photo -> {
                        val state = (overlay.value as ProfileOverlayState.Photo)
                        PhotoOverlay(
                            author = userId,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStore.get(userId)
                        )
                    }
                    is ProfileOverlayState.Video -> {
                        val state = (overlay.value as ProfileOverlayState.Video)
                        VideoOverlay(
                            author = userId,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStore.get(userId)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
