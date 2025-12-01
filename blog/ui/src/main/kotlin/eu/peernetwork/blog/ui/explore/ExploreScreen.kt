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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostType
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun ExploreScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Explore.Component, ExploreViewModel) -> Unit
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
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
fun ExploreScreen(
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (UiPost, Int) -> Unit
) {
    ExploreScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val isLoading = remember { derivedStateOf { state == ExploreViewModel.State.Loading  } }
        val isRefreshing = remember { mutableStateOf(isLoading.value) }
        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    ExploreViewModel.State.Empty -> DesignStreamState.Default
                    ExploreViewModel.State.Loading -> DesignStreamState.Loading
                    is ExploreViewModel.State.Success -> DesignStreamState.Success(
                        (state as ExploreViewModel.State.Success).content
                    )
                    is ExploreViewModel.State.Error -> DesignStreamState.Error(
                        (state as ExploreViewModel.State.Error).error
                    )
                }
            }
        }
        DesignRefreshScaffold(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.get(Pageable(0, limit)) }
        ) {
            DesignPagingStream(
                state = derivedState,
                loading = {  }
            ) { lazyPagingItems ->
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        val post = lazyPagingItems[index]
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
        LaunchedEffect(Unit)  {
            if (state is ExploreViewModel.State.Empty) {
                viewModel.get(Pageable(0, limit))
            }
        }
    }
}
