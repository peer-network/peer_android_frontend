package eu.peernetwork.blog.ui.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ArticleModal(
    author: String,
    username: String,
    imageUrl: String,
    types: Set<Content.Type>,
    limit: Int,
    selected: MutableIntState,
    timestamp: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit
) {
    ArticleFullScreen(
        id = author,
        username = username,
        imageUrl = imageUrl,
        types = types,
        limit = limit,
        selected = selected,
        timestamp = timestamp,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
    ) { component, item, index, pagerState ->
        GalleryScreen(
            position = index,
            current = selected,
            enabled = !pagerState.isScrollInProgress,
            post = item
        ) {}
    }
}
