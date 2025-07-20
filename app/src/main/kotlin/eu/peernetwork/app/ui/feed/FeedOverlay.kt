package eu.peernetwork.app.ui.feed

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.blog.ui.timeline.photo.PhotoOverlay
import eu.peernetwork.blog.ui.timeline.video.VideoOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
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
    viewModelStore: ViewModelState,
    connectionController: ConnectionController,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is FeedOverlayState.Empty) }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = FeedOverlayState.Empty }
    ) { controller ->
        val overlayState = remember { mutableStateOf<FeedOverlayState?>(overlay.value) }
        FeedNavigation(
            userId = userId,
            startDestination = "overlay",
            postLimit = postLimit,
            controller = controller,
            component = component,
            viewModelStore = viewModelStore,
        ) {
            when (overlayState.value) {
                is FeedOverlayState.Photo -> {
                    val state = (overlayState.value as FeedOverlayState.Photo)
                    PhotoOverlay(
                        userId,
                        postLimit,
                        state.position,
                        Relation.NONE,
                        criteria,
                        component,
                        viewModelStore.get(criteria?.toString() ?: userId),
                        onMentionClick = { controller.navigateToUsernameSearch(it) },
                        onHashtagClick = { controller.navigateToTagSearch(it) },
                        onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                    ) {
                        ConnectionScreen(
                            isFollowing = connection.getOrDefault(it.first, it.third),
                            isFollowed = it.second,
                            onClick = { follow -> connectionController.invoke(it.first, !follow) }
                        )
                    }
                }
                is FeedOverlayState.Video -> {
                    val state = (overlayState.value as FeedOverlayState.Video)
                    VideoOverlay(
                        postLimit,
                        state.position,
                        visible.value,
                        Relation.NONE,
                        criteria,
                        component,
                        viewModelStore.get(criteria?.toString() ?: userId),
                        onPostClick = { id, index ->
                            overlay.value = FeedOverlayState.Video(id, index) },
                        onMentionClick = { controller.navigateToUsernameSearch(it) },
                        onHashtagClick = { controller.navigateToTagSearch(it) },
                        onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                    ) {
                        ConnectionScreen(
                            isFollowing = connection.getOrDefault(it.first, it.third),
                            isFollowed = it.second,
                            onClick = { follow -> connectionController.invoke(it.first, !follow) }
                        )
                    }
                }
                else -> {}
            }
            DesignTitleBarHost("FeedOverlay$userId") {
                titleBar {
                    DesignTitle {
                        Text(stringResource(eu.peernetwork.user.ui.R.string.feed_label))
                    }
                }
            }
        }
    }
}
