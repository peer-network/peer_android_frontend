package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.app.ui.window.WindowTitle
import eu.peernetwork.blog.domain.usecase.PhotosUsecase
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.post.photo.PhotoOverlay
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlayPage
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
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
    connectionController: State<ConnectionController>,
    provider: UiComponentProvider,
    component: Profile.Component,
    viewModelStore: UiViewModelStore,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
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
            val event = remember {
                object : UiPostEvent {
                    override fun onMentionClick(username: String) = controller.navigateToUsernameSearch(username)

                    override fun onHashtagClick(tag: String) = controller.navigateToTagSearch(tag)

                    override fun onPostClick(id: String, position: Int) {
                        overlay.value = ProfileOverlayState.Photo(id, position)
                    }

                    override fun onMediaClick(id: String, position: Int) {
                        overlay.value = ProfileOverlayState.Video(id, position)
                    }

                    override fun onAuthorClick(id: String) = controller.navigateIfNecessary("profile/$id")
                }
            }
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
                            types = PhotosUsecase.POST,
                            enabled = visible.value,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStoreOwner = viewModelStore.get("$userId${PhotosUsecase.POST}"),
                            event = event,
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
                                    onClick = { follow -> connectionController.value.invoke(it.first, !follow) }
                                )
                            }
                        }
                    }
                    is ProfileOverlayState.Video -> {
                        val state = (overlayState.value as ProfileOverlayState.Video)
                        PhotoOverlay(
                            author = userId,
                            types = PhotosUsecase.MEDIA,
                            enabled = visible.value,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStoreOwner = viewModelStore.get("$userId${PhotosUsecase.MEDIA}"),
                            event = event,
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
                                    onClick = { follow -> connectionController.value.invoke(it.first, !follow) }
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
