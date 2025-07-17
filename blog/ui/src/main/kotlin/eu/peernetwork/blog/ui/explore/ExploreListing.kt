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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun ExploreListing(
    component: Explore.Component,
    lazyPagingItems: LazyPagingItems<UiPost>,
    state: LazyGridState = rememberLazyGridState(),
    onClick: (UiPost, Int) -> Unit
) {
    val handleClick by rememberUpdatedState(onClick)
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val post = lazyPagingItems[index]
            if (post?.type == UiPost.Type.IMAGE) {
                Box(
                    modifier = Modifier.aspectRatio(1f)
                        .clickable { handleClick(post, index) }
                ) {
                    component.imageView()(
                        Modifier,
                        ImageView.Spec(
                            post.media.first().path,
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
