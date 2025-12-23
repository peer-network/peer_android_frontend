package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationInteractor.Companion.LocalModerationInteractor
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.timeline.TimelineSheet
import eu.peernetwork.blog.ui.timeline.TimelineSheetMenuItem
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
            loading = { ExploreSkeleton(3) }
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
    uuid: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    showSheet: MutableState<UiPost?>,
    limit: Int,
    component: Explore.Component,
    viewModel: ExploreViewModel,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBoost: (String) -> Unit,
    content: @Composable PagerScope.(Explore.Component, UiPost, Int, PagerState) -> Unit
) {
    val context = LocalContext.current
    val handleBoost by rememberUpdatedState(onBoost)
    val updatedContent by rememberUpdatedState(content)
    val shareTitle = stringResource(R.string.share_label)
    ExploreScreen(
        limit = limit,
        component = component,
        viewModel = viewModel,
    ) { component, items ->
        val pagerState = rememberPagerState(initialPage = selected.intValue) { items.itemCount }
        PostScreen(
            uuid = uuid,
            username = username,
            imageUrl = imageUrl,
            limit = limit,
            pagerState = pagerState,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onComment = { items.itemSnapshotList.getOrNull(it)?.mapToDetail() },
            modal = {
                val moderation = LocalModerationInteractor.current
                TimelineSheet(
                    uuid = uuid,
                    state = showSheet
                ) { sheetState, post ->
                    when (sheetState) {
                        TimelineSheetMenuItem.REPORT -> moderation.onReport(post.id)
                        TimelineSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
                        TimelineSheetMenuItem.BOOST -> { handleBoost(post.id) }
                    }
                }
            }
        ) { index ->
            items[index]?.let { updatedContent(this, component, it, index, pagerState) }
            LaunchedEffect(Unit) {
                items.itemSnapshotList.getOrNull(pagerState.currentPage)?.let { post ->
                    viewModel.view(post.id)
                }
            }
        }
    }
}
