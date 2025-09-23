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
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.feed.timeline.PostOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

sealed interface FeedOverlayState {
    data object Empty : FeedOverlayState

    data class Post(
        val id: String,
        val position: Int,
        val category: Category,
        val criteria: Criteria? = null
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
    val visible = remember(overlay.value) {
        mutableStateOf(overlay.value !is FeedOverlayState.Empty)
    }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = FeedOverlayState.Empty }
    ) { controller ->
        val overlayState = remember { mutableStateOf<FeedOverlayState?>(overlay.value) }
        val event = remember {
            object : UiPostListener {
                override fun invoke(event: UiPostListener.Event) {
                    when(event) {
                        is UiPostListener.Event.Mention -> {
                            controller.navigateToUsernameSearch(event.username)
                        }
                        is UiPostListener.Event.Hashtag -> {
                            controller.navigateToTagSearch(event.tag)
                        }
                        is UiPostListener.Event.Author -> {
                            controller.navigateIfNecessary("profile/${event.id}")
                        }
                        else -> {}
                    }
                }
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
            val state = (overlayState.value as FeedOverlayState.Post)
            val storeKey = "${state.category};${state.criteria?.toString() ?: userId}"
            PostOverlay(
                id = userId,
                limit = postLimit,
                enabled = visible.value,
                position = state.position,
                category = state.category,
                criteria = criteria,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(storeKey),
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
                    onClick = { follow ->
                        connectionController.value.invoke(it.first, !follow)
                    }
                )
            }
        }
    }
}
