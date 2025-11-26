package eu.peernetwork.blog.ui.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.post.PostModal
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ArticleModal(
    author: String,
    types: Set<Content.Type>,
    limit: Int,
    selected: MutableIntState,
    timestamp: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit
) {
    ArticleScreen(
        id = author,
        types = types,
        limit = limit,
        selected = selected,
        timestamp = timestamp,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent
    ) { component, items, index ->
        items[index]?.title?.text?.let {
            PostModal(it)
        }
    }
}
