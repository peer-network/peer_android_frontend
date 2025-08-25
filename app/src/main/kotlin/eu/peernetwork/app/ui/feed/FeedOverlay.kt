package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.app.ui.window.WindowTitle
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.timeline.photo.PhotoOverlay
import eu.peernetwork.blog.ui.timeline.video.VideoOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

sealed interface FeedOverlayState {
    data object Empty : FeedOverlayState

    data class Photo(
        val id: String,
        val position: Int
    ) : FeedOverlayState

    data class Video(
        val id: String,
        val position: Int
    ) : FeedOverlayState
}

@Composable
fun FeedOverlay(
    overlay: MutableState<FeedOverlayState>,
    userId: String,
    postLimit: Int,
    criteria: Criteria? = null,
    component: Feed.Component,
    viewModelStore: UiViewModelStore,
    connectionController: State<ConnectionController>,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is FeedOverlayState.Empty) }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = FeedOverlayState.Empty }
    ) { controller ->
        val overlayState = remember { mutableStateOf<FeedOverlayState?>(overlay.value) }
        val event = remember {
            object : UiPostEvent {
                override fun onMentionClick(username: String) = controller.navigateToUsernameSearch(username)

                override fun onHashtagClick(tag: String) = controller.navigateToTagSearch(tag)

                override fun onPostClick(id: String, position: Int) {
                    overlay.value = FeedOverlayState.Photo(id, position)
                }

                override fun onVideoClick(id: String, position: Int) {
                    overlay.value = FeedOverlayState.Video(id, position)
                }

                override fun onAuthorClick(id: String) = controller.navigateIfNecessary("profile/$id")
            }
        }
        FeedNavigation(
            userId = userId,
            startDestination = "overlay",
            postLimit = postLimit,
            controller = controller,
            component = component,
            viewModelStore = viewModelStore,
            onCancel = { visible.value = false }
        ) {
            when (overlayState.value) {
                is FeedOverlayState.Photo -> {
                    val state = (overlayState.value as FeedOverlayState.Photo)
                    PhotoOverlay(
                        id = userId,
                        limit = postLimit,
                        position = state.position,
                        category = Category.ALL,
                        criteria = criteria,
                        provider = component,
                        viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: userId),
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
                        ConnectionScreen(
                            isFollowing = connection.getOrDefault(it.first, it.third),
                            isFollowed = it.second,
                            onClick = { follow -> connectionController.value.invoke(it.first, !follow) }
                        )
                    }
                }
                is FeedOverlayState.Video -> {
                    val state = (overlayState.value as FeedOverlayState.Video)
                    VideoOverlay(
                        limit = postLimit,
                        position = state.position,
                        enabled = visible.value,
                        category = Category.ALL,
                        criteria = criteria,
                        provider = component,
                        viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: userId),
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
                        ConnectionScreen(
                            isFollowing = connection.getOrDefault(it.first, it.third),
                            isFollowed = it.second,
                            onClick = { follow -> connectionController.value.invoke(it.first, !follow) }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
