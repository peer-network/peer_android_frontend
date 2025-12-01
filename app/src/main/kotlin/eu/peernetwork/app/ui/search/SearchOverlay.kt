package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.ui.window.WindowTitle
import eu.peernetwork.blog.ui.explore.ExploreOverlay
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionInteractor
import eu.peernetwork.social.ui.connection.ConnectionScreen

sealed interface SearchOverlayState {
    data object Empty : SearchOverlayState
    data class Photo(
        val id: String,
        val position: Int
    ) : SearchOverlayState
}

@Composable
fun SearchOverlay(
    id: String,
    limit: Int,
    overlay: MutableState<SearchOverlayState>,
    component: Search.Component,
    viewModelStore: UiViewModelStore,
    connectionController: ConnectionInteractor,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is SearchOverlayState.Empty) }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = SearchOverlayState.Empty }
    ) { controller ->
        val overlayState = remember { mutableStateOf<SearchOverlayState?>(overlay.value) }
//        val event = remember {
//            object : UiPostListener {
//                override fun invoke(event: UiPostListener.Event) {
//                    when(event) {
//                        is UiPostListener.Event.Mention -> {
//                            controller.navigateToUsernameSearch(event.username)
//                        }
//                        is UiPostListener.Event.Hashtag -> {
//                            controller.navigateToTagSearch(event.tag)
//                        }
//                        is UiPostListener.Event.Author -> {
//                            controller.navigateIfNecessary("profile/${event.id}")
//                        }
//                        is UiPostListener.Event.Post -> {
//                            overlay.value = SearchOverlayState.Photo(event.id, event.position)
//                        }
//                    }
//                }
//            }
//        }
        SearchNavigation(
            userId = id,
            startDestination = "overlay",
            component = component,
            viewModelStore = viewModelStore,
            controller = controller,
            onCancel = { visible.value = false }
        ) { backStackEntry, controller ->
            val state = (overlayState.value as SearchOverlayState.Photo)
            ExploreOverlay(
                author = id,
                limit = limit,
                position = state.position,
                enabled = visible.value,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(id),
                header = {
                    WindowTitle(
                        provider = component,
                        viewModelStoreOwner = backStackEntry,
                        onCancel = { visible.value = false },
                    )
                }
            ) {
                ConnectionScreen(
                    isFollowing = connection.getOrDefault(it.first, it.third),
                    isFollowed = it.second,
                    onClick = { follow -> connectionController.invoke(it.first, !follow) }
                )
            }
        }
    }
}
