package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.mapper.v2.mapToDetail
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

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
    component: Explore.Component,
    viewModel: ExploreViewModel,
    content: @Composable (Explore.Component, LazyPagingItems<UiPost>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
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
            updatedContent(component, lazyPagingItems)
        }
    }
    LaunchedEffect(Unit)  {
        if (state is ExploreViewModel.State.Empty) {
            viewModel.get(Pageable(0, limit))
        }
    }
}

@Composable
fun ExploreFullScreen(
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    component: Explore.Component,
    viewModel: ExploreViewModel,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable PagerScope.(Explore.Component, UiPost, Int, PagerState) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val key = component.hashCode()
    ExploreScreen(
        limit = limit,
        component = component,
        viewModel = viewModel,
    ) { component, items ->
        val pagerState = rememberPagerState(initialPage = selected.intValue) { items.itemCount }
        PostScreen(
            username = username,
            imageUrl = imageUrl,
            limit = limit,
            pagerState = pagerState,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onComment = { items.itemSnapshotList.getOrNull(it)?.mapToDetail() }
        ) { index ->
            items[index]?.let { updatedContent(this, component, it, index, pagerState) }
            LaunchedEffect(Unit) {
                items.itemSnapshotList.getOrNull(pagerState.currentPage)?.let { post ->
                    viewModel.view(post.id)
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.selected(key, -1)
            selected.intValue = -1
        }
    }
}
