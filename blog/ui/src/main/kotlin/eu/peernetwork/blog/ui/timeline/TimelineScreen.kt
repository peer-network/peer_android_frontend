package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.post.PostInteractor
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun TimelineScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Timeline.Component, TimelineViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Timeline.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TimelineViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun TimelineScreen(
    limit: Int,
    category: Category,
    criteria: Filter.Criteria? = null,
    component: Timeline.Component,
    viewModel: TimelineViewModel,
    loading: @Composable () -> Unit,
    content: @Composable (Timeline.Component, LazyPagingItems<UiPost>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val key = listOf(category, criteria).hashCode()
    val localState = remember(key) { derivedStateOf {
        state[key] ?: TimelineViewModel.State.Empty
    } }
    val derivedState = remember(localState.value) {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                TimelineViewModel.State.Empty -> DesignStreamState.Default
                TimelineViewModel.State.Loading -> DesignStreamState.Loading
                is TimelineViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is TimelineViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error
                )
            }
        }
    }
    DesignPagingStream(
        state = derivedState,
        loading = loading
    ) { updatedContent(component, it) }
    LaunchedEffect(category, criteria) {
        val currentState = localState.value as? TimelineViewModel.State.Success?
        val requiresChange = currentState?.category != category
                || currentState.criteria != criteria
        if (requiresChange) {
            viewModel.load(Pageable(0, limit), category, criteria)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun TimelineScreen(
    limit: Int,
    category: Category,
    criteria: Filter.Criteria? = null,
    focused: MutableIntState,
    showSheet: MutableState<UiPost?>,
    listState: LazyListState = rememberLazyListState(),
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: LazyListScope.(Timeline.Component, PostInteractor, LazyPagingItems<UiPost>) -> Unit
) {
    val context = LocalContext.current
    val updatedContent by rememberUpdatedState(content)
    val shareTitle = stringResource(R.string.share_label)
    TimelineScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val page = Pageable(0, limit)
        val state by viewModel.state.collectAsStateWithLifecycle()
        val key = listOf(category, criteria).hashCode()
        val isLoading = remember { derivedStateOf { state[key] == TimelineViewModel.State.Loading  } }
        val isRefreshing = remember { mutableStateOf(isLoading.value) }
        DesignRefreshScaffold(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.load(
                    page = page,
                    category = category,
                    criteria = criteria
                )
            }
        ) {
            TimelineScreen(
                limit = limit,
                category = category,
                criteria = criteria,
                component = component,
                viewModel = viewModel,
                loading = { PostSkeleton(3) },
            ) { component, items ->
                PostScreen(
                    limit = limit,
                    focused = focused,
                    listState = listState,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onFocus = {
                        items.itemSnapshotList.get(it)?.let { post ->
                            viewModel.view(post.id)
                        }
                    }
                ) { interactor ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) { updatedContent(this, component, interactor, items) }
                    TimelineSheet(showSheet) { sheetState, post ->
                        when (sheetState) {
                            TimelineSheetMenuItem.REPORT -> interactor.moderation().onReport(post.id)
                            TimelineSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
                        }
                    }
                }
            }
        }
    }
}
