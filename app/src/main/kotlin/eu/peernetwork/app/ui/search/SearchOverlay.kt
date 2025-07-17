package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.ui.feed.navigateToTagSearch
import eu.peernetwork.app.ui.feed.navigateToUsernameSearch
import eu.peernetwork.blog.ui.explore.ExploreOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
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
    viewModelStore: ViewModelState,
    connectionController: ConnectionController,
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
        SearchNavigation(
            userId = id,
            startDestination = "overlay",
            component = component,
            viewModelStore = viewModelStore,
            controller = controller
        ) {
            val state = (overlayState.value as SearchOverlayState.Photo)
            ExploreOverlay(
                id,
                limit,
                state.position,
                component,
                viewModelStore.get(id),
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
    }
}
