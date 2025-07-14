package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder


@Composable
fun ExploreScreen(
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Explore.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ExploreViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)

    LaunchedEffect(Unit) {
        viewModel.get(page = Pageable(0, postLimit))
    }

    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ExploreViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ExploreViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ExploreViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as ExploreViewModel.State.Success).content
                )

                is ExploreViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as ExploreViewModel.State.Error).error.let {
                        Throwable(component.resource().string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    DesignPagingScaffold<UiPost>(
        state = derivedState,
        onRefresh = { viewModel.get(Pageable(0, postLimit)) },
        placeholder = { PostPageSkeleton() },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { state, lazyPagingItems ->
        val refreshState = remember {
            derivedStateOf {
                if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                    DesignStatefulScaffoldState.Loading
                } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                    DesignStatefulScaffoldState.Error(
                        (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    )
                } else {
                    state.value
                }
            }
        }
        DesignRefreshableScaffold<LazyPagingItems<UiPost>>(
            state = refreshState,
            onRefresh = { lazyPagingItems.refresh() }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    val post = lazyPagingItems[index]
                    if (post?.type == UiPost.Type.IMAGE) {
                        ExplorePhotoItem(post = post, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun ExplorePhotoItem(post: UiPost, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.media.first().path)
                .crossfade(true)
                .size(300)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
