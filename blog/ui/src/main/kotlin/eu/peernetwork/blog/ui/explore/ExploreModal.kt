package eu.peernetwork.blog.ui.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ExploreModal(
    uuid: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBoost: (String) -> Unit
) {
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    ExploreScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        ExploreFullScreen(
            uuid = uuid,
            username = username,
            imageUrl = imageUrl,
            showSheet = showSheet,
            selected = selected,
            limit = limit,
            component = component,
            viewModel = viewModel,
            viewModelStoreOwner = viewModelStoreOwner,
            onBoost = onBoost
        ) { component, item, index, pagerState ->
            val enabled = remember { derivedStateOf { pagerState.currentPage == index } }
            GalleryScreen(
                uuid = uuid,
                position = index,
                enabled = enabled,
                post = item,
                showSheet = showSheet
            ) {}
        }
    }
}
