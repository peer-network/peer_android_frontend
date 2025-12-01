package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.post.PostInteractor
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Article.Component, ArticleViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Article.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = ArticleViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleScreen(
    id: String,
    limit: Int,
    types: Set<Content.Type>,
    selected: MutableIntState,
    timestamp: State<Long>,
    component: Article.Component,
    viewModel: ArticleViewModel,
    onEvent: (ArticleEvent) -> Unit,
    loading: @Composable () -> Unit = {},
    content: @Composable (Article.Component, LazyPagingItems<UiPost>) -> Unit
) {
    val state by viewModel.states.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val localState = remember { derivedStateOf {
        state[types.hashCode()] ?: ArticleViewModel.State.Empty
    } }
    val localStatus = remember { derivedStateOf {
        status[types.hashCode().toString()] ?: ArticleViewModel.Status.Empty
    } }
    val selector = remember { derivedStateOf {
        status[id] ?: ArticleViewModel.Status.Empty
    } }
    val position = remember { derivedStateOf {
        (selector.value as? ArticleViewModel.Status.Success<Int>?)?.data ?: -1
    } }
    val derivedState = remember {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                ArticleViewModel.State.Empty -> DesignStreamState.Default
                ArticleViewModel.State.Loading -> DesignStreamState.Loading
                is ArticleViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is ArticleViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error
                )
            }
        }
    }
    val handleEvent by rememberUpdatedState(onEvent)
    val updatedContent by rememberUpdatedState(content)
    DesignPagingStream(
        state = derivedState,
        loading = loading
    ) { updatedContent(component, it) }
    LaunchedEffect(timestamp.value) {
        val page = Pageable(0, limit)
        (localStatus.value as? ArticleViewModel.Status.Success<Long>?)?.let {
            if (it.data != timestamp.value) {
                viewModel.load(id, types, page)
            }
        } ?: viewModel.load(id, types, page)
        viewModel.updatedAt(types.hashCode(), timestamp.value)
    }
    LaunchedEffect(selected.intValue) {
        if (selected.intValue != position.value) {
            viewModel.selected(id, selected.intValue)
            handleEvent(ArticleEvent.Post(selected.intValue))
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.selected(id, -1)
            selected.intValue = -1
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleScreen(
    id: String,
    types: Set<Content.Type>,
    limit: Int,
    focused: MutableIntState,
    selected: MutableIntState,
    timestamp: State<Long>,
    showSheet: MutableState<UiPost?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.(Article.Component, PostInteractor, LazyPagingItems<UiPost>) -> Unit
) {
    val context = LocalContext.current
    val updatedContent by rememberUpdatedState(content)
    val handleEvent by rememberUpdatedState(onEvent)
    val shareTitle = stringResource(R.string.share_label)
    ArticleScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        ArticleScreen(
            id = id,
            types = types,
            limit = limit,
            selected = selected,
            timestamp = timestamp,
            component = component,
            viewModel = viewModel,
            onEvent = onEvent,
            loading = { PostSkeleton(3) }
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
                ArticleSheet(showSheet) { sheetState, post ->
                    when (sheetState) {
                        ArticleSheetMenuItem.BOOST -> handleEvent(ArticleEvent.Boost(post.id))
                        ArticleSheetMenuItem.REPORT -> interactor.moderation().onReport(post.id)
                        ArticleSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleScreen(
    id: String,
    types: Set<Content.Type>,
    limit: Int,
    selected: MutableIntState,
    timestamp: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit,
    content: @Composable PagerScope.(Article.Component, LazyPagingItems<UiPost>, Int) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    ArticleScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        ArticleScreen(
            id = id,
            types = types,
            limit = limit,
            selected = selected,
            timestamp = timestamp,
            component = component,
            viewModel = viewModel,
            onEvent = onEvent,
            loading = { PostSkeleton(3) }
        ) { component, items ->
            val pagerState = rememberPagerState(initialPage = selected.intValue) { items.itemCount }
            PostScreen(
                limit = limit,
                pagerState = pagerState,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { interactor, index ->
                updatedContent(this, component, items, index)
                LaunchedEffect(Unit) {
                    items.itemSnapshotList.get(pagerState.currentPage)?.let { post ->
                        viewModel.view(post.id)
                    }
                }
            }
        }
    }
}
