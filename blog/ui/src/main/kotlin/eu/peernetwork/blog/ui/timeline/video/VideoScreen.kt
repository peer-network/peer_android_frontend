package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.media.core.model.UiMimeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    id: String,
    postLimit: Int,
    relation: Relation,
    criteria: Criteria? = null,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
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
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle().value
    var lastRelation = rememberSaveable { mutableStateOf(relation) }
    DesignPagingScaffold<UiVideo>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit), relation, criteria) },
        placeholder = { PostPageSkeleton() },
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
                        component = component,
                        listState = listState,
                        lazyPagingItems = lazyPagingItems,
                        engagement = engagement,
                        moderation = moderation,
                        onPostClick = onPostClick,
                        onLoadBitmap = { thumbnail[it] },
                        onLoad = { url, ratio ->
                            viewModel.thumbnail(
                                url,
                                UiMimeType.Video,
                                configuration.screenWidthDp,
                                ratio) },
                        onAuthorClick = onAuthorClick,
                        onHashtagClick = onHashtagClick,
                        onMentionClick = onMentionClick,
                        connection = connection
                    )
                }
            }
        }
    }
    LaunchedEffect(relation, criteria) {
        if (relation != lastRelation.value) {
            viewModel.load(Pageable(0, postLimit), relation, criteria)
            lastRelation.value = relation
        }
    }
}
