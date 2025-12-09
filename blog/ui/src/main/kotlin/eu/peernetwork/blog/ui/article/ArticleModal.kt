package eu.peernetwork.blog.ui.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.blog.ui.model.v2.UiPost
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
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    ArticleFullScreen(
        id = author,
        username = username,
        imageUrl = imageUrl,
        types = types,
        limit = limit,
        selected = selected,
        timestamp = timestamp,
        showSheet = showSheet,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
    ) { component, item, index, pagerState ->
        GalleryScreen(
            position = index,
            selected = selected,
            showSheet = showSheet,
            enabled = !pagerState.isScrollInProgress,
            post = item
        ) {}
    }
}
