package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    id: String,
    enable: Boolean,
    postLimit: Int,
    category: Category,
    criteria: Criteria? = null,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    requireUpdate: MutableState<Boolean>,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val coroutine = rememberCoroutineScope()
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as VideoViewModel.State.Success).data
                )
                is VideoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as VideoViewModel.State.Error).error
                )
            }
        }
    }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val canLoad = remember { derivedStateOf {
        listState.layoutInfo.totalItemsCount > 0 && !listState.isScrollInProgress
    } }
    DesignPagingScaffold<UiVideo>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit), category, criteria) },
        placeholder = { PostPlaceholder() },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { state, lazyPagingItems ->
        val refreshState = remember { derivedStateOf {
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStatefulScaffoldState.Loading
            } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                DesignStatefulScaffoldState.Error(
                    (lazyPagingItems.loadState.refresh as LoadState.Error).error
                )
            } else {
                state.value
            }
        } }
        val refreshed = remember { derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        } }
        DesignRefreshableScaffold<LazyPagingItems<UiPost>>(
            state = refreshState,
            onRefresh = { lazyPagingItems.refresh() }
        ) {
            EngagementScreen(
                postLimit,
                refreshed,
                onMentionClick,
                onHashtagClick,
                onAuthorClick,
                component,
                viewModelStoreOwner
            ) { engagement ->
                ModerationScreen(
                    component,
                    viewModelStoreOwner
                ) { moderation ->
                    VideoListing(
                        id = id,
                        enable = enable,
                        component = component,
                        listState = listState,
                        lazyPagingItems = lazyPagingItems,
                        engagement = engagement,
                        moderation = moderation,
                        onPostClick = onPostClick,
                        onLoadBitmap = { thumbnail.value[it] },
                        onAuthorClick = onAuthorClick,
                        onHashtagClick = onHashtagClick,
                        onMentionClick = onMentionClick,
                        connection = connection
                    )
                }
            }
        }
        LaunchedEffect(requireUpdate.value) {
            if (requireUpdate.value) {
                lazyPagingItems.refresh()
                coroutine.launch {
                    listState.animateScrollToItem(0)
                }
                requireUpdate.value = false
            }
        }
        LaunchedEffect(canLoad.value) {
            if (canLoad.value) {
                viewModel.sync(
                    lazyPagingItems.itemSnapshotList.items,
                    UiMimeType.Video,
                    configuration.screenWidthDp,
                    listState.firstVisibleItemIndex,
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: listState.firstVisibleItemIndex
                )
            }
        }
    }
    LaunchedEffect(category, criteria) {
        if (category != viewModel.lastCategory) {
            viewModel.load(Pageable(0, postLimit), category, criteria)
        }
    }
}
