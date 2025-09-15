package eu.peernetwork.app.ui.search

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
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.explore.ExploreOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
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
    connectionController: State<ConnectionController>,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is SearchOverlayState.Empty) }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = SearchOverlayState.Empty }
    ) { controller ->
        val overlayState = remember { mutableStateOf<SearchOverlayState?>(overlay.value) }
        val event = remember {
            object : UiPostListener {
                override fun onMentionClick(username: String) = controller.navigateToUsernameSearch(username)

                override fun onHashtagClick(tag: String) = controller.navigateToTagSearch(tag)

                override fun onPostClick(id: String, position: Int) {
                    overlay.value = SearchOverlayState.Photo(id, position)
                }
                override fun onAuthorClick(id: String) = controller.navigateIfNecessary("profile/$id")
            }
        }
        SearchNavigation(
            userId = id,
            startDestination = "overlay",
            component = component,
            viewModelStore = viewModelStore,
            controller = controller,
            onCancel = { visible.value = false }
        ) {
            val state = (overlayState.value as SearchOverlayState.Photo)
            ExploreOverlay(
                author = id,
                limit = limit,
                position = state.position,
                enabled = visible.value,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(id),
                event = event,
                header = {
                    WindowTitle(
                        id = id,
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
    }
}
