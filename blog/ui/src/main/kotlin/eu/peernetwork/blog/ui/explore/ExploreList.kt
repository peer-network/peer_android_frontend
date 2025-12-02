package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostType
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun ExploreList(
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (UiPost, Int) -> Unit
) {
    ExploreScreen(
        limit = limit,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, items ->
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items.itemCount) { index ->
                val post = items[index]
                if (post?.type == UiPostType.IMAGE) {
                    Box(
                        modifier = Modifier.aspectRatio(1f)
                            .clickable {  }
                    ) {
                        component.imageView()(
                            Modifier,
                            ImageView.Spec(
                                post.asset.media.first().path,
                                null,
                                ContentScale.Crop,
                                width = 250
                            )
                        )
                    }
                }
            }
        }
    }
}
