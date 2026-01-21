package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.timeline.TimelineViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.media.core.renderer.ImageView

@Composable
@Suppress("UNCHECKED_CAST")
fun ExploreList(
    uuid: String,
    limit: Int,
    selected: MutableIntState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyGridState = rememberLazyGridState(),
    onShow: (Int) -> Unit
) {
    val handleShow by rememberUpdatedState(onShow)
    ExploreScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val key = component.hashCode()
        val status by viewModel.status.collectAsStateWithLifecycle()
        val selector = remember { derivedStateOf {
            status[key] ?: TimelineViewModel.Status.Empty
        } }
        val position = remember { derivedStateOf {
            (selector.value as? ExploreViewModel.Status.Success<Int>?)?.data ?: -1
        } }
        ExploreScreen(
            limit = limit,
            component = component,
            viewModel = viewModel,
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
                                .background(MaterialTheme.colorScheme.surfaceDim)
                                .clickable { selected.intValue = index }
                        ) {
                            ExploreMask(
                                status = post.status,
                                isAuthor = post.author.id == uuid,
                                isAccessible = post.isAccessible,
                                placeholder = {
                                    component.imageView()(
                                        modifier = Modifier,
                                        spec = ImageView.Spec(
                                            url = post.asset.media.first().path,
                                            ratio = post.asset.ratio,
                                            contentScale = ContentScale.Crop,
                                            blur = 500f,
                                        )
                                    )
                                }
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
        LaunchedEffect(selected.intValue) {
            if (selected.intValue != position.value) {
                viewModel.selected(key, selected.intValue)
                if (selected.intValue != -1) {
                    handleShow(selected.intValue)
                }
            }
        }
    }
}
