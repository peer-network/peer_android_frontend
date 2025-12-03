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
    ExploreScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        ExploreFullScreen(
            username = username,
            imageUrl = imageUrl,
            selected = selected,
            limit = limit,
            component = component,
            viewModel = viewModel,
            viewModelStoreOwner = viewModelStoreOwner
        ) { component, item, index, pagerState ->
            PostModal(
                position = index,
                current = selected,
                enabled =!pagerState.isScrollInProgress,
                item
            )
        }
    }
}
