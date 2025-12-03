package eu.peernetwork.blog.ui.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.ui.post.PostModal
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TimelineModal(
    selected: MutableIntState,
    limit: Int,
    category: Category,
    criteria: Filter.Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    TimelineFullScreen(
        limit = limit,
        selected = selected,
        category = category,
        criteria = criteria,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, items, index ->
        items[index]?.title?.text?.let {
            PostModal(it)
        }
    }
}
