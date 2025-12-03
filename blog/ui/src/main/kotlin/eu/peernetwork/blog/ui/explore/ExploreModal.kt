package eu.peernetwork.blog.ui.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.post.PostModal
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ExploreModal(
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    ExploreFullScreen(
        username = username,
        imageUrl = imageUrl,
        selected = selected,
        limit = limit,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, items, index ->
        items[index]?.title?.text?.let {
            PostModal(it)
        }
    }
}
