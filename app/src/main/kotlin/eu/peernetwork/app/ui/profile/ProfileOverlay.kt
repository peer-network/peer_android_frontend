package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.ui.feed.navigateToTagSearch
import eu.peernetwork.app.ui.feed.navigateToUsernameSearch
import eu.peernetwork.app.ui.window.WindowTitle
import eu.peernetwork.blog.ui.post.photo.PhotoOverlay
import eu.peernetwork.blog.ui.post.video.VideoOverlay
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlayPage
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

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
    principal: String,
    userId: String,
    limit: Int,
    connectionController: ConnectionController,
    provider: UiComponentProvider,
    component: Profile.Component,
    viewModelStore: ViewModelState,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is ProfileOverlayState.Empty) }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = ProfileOverlayState.Empty }
    ) { controller ->
        DesignOverlayPage(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val overlayState = remember { mutableStateOf<ProfileOverlayState?>(overlay.value) }
            ProfileNavigation(
                principal = principal,
                userId = userId,
                startDestination = "overlay",
                controller = controller,
                provider = provider,
                component = component,
                viewModelStore = viewModelStore,
                onCancel = { visible.value = false }
            ) {
                when (overlayState.value) {
                    is ProfileOverlayState.Photo -> {
                        val state = (overlayState.value as ProfileOverlayState.Photo)
                        PhotoOverlay(
                            author = userId,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStore.get(userId),
                            onMentionClick = { controller.navigateToUsernameSearch(it) },
                            onHashtagClick = { controller.navigateToTagSearch(it) },
                            onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                            header = {
                                WindowTitle(
                                    id = userId,
                                    provider = component,
                                    viewModelStore = viewModelStore,
                                    onCancel = { visible.value = false },
                                )
                            }
                        ) {
                            if (userId != principal) {
                                ConnectionScreen(
                                    isFollowing = connection.getOrDefault(it.first, it.third),
                                    isFollowed = it.second,
                                    onClick = { follow -> connectionController.invoke(it.first, !follow) }
                                )
                            }
                        }
                    }
                    is ProfileOverlayState.Video -> {
                        val state = (overlayState.value as ProfileOverlayState.Video)
                        VideoOverlay(
                            author = userId,
                            enable = visible.value,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStore.get(userId),
                            onPostClick = { id, index ->
                                overlay.value = ProfileOverlayState.Video(id, index) },
                            onMentionClick = { controller.navigateToUsernameSearch(it) },
                            onHashtagClick = { controller.navigateToTagSearch(it) },
                            onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                            header = {
                                WindowTitle(
                                    id = userId,
                                    provider = component,
                                    viewModelStore = viewModelStore,
                                    onCancel = { visible.value = false },
                                )
                            }
                        ) {
                            if (userId != principal) {
                                ConnectionScreen(
                                    isFollowing = connection.getOrDefault(it.first, it.third),
                                    isFollowed = it.second,
                                    onClick = { follow -> connectionController.invoke(it.first, !follow) }
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
