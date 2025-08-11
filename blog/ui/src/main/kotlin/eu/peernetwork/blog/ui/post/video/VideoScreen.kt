package eu.peernetwork.blog.ui.post.video

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun VideoScreen(
    author: String,
    enable: State<Boolean>,
    postLimit: Int,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    listState: LazyListState = rememberLazyListState(),
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is VideoViewModel.State.Success -> {
                DesignStatefulScaffoldState.Success(
                    (state as VideoViewModel.State.Success).content
                )
            }
            is VideoViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as VideoViewModel.State.Error).error)
            }
        }
    } }
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    DesignPagingScaffold(
        state = derivedState,
        placeholder = { PostPlaceholder() },
        onRefresh = { viewModel.load(author, Pageable(0, postLimit)) },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { contentState, lazyPagingItems ->
        val refreshed = remember { derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        } }
        EngagementScreen(
            postLimit = postLimit,
            refresh = refreshed,
            onMentionClick = event::onMentionClick,
            onHashtagClick = event::onHashtagClick,
            onAuthorClick = event::onAuthorClick,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { engagement ->
            ModerationScreen(
                component,
                viewModelStoreOwner
            ) { moderation ->
                VideoListing(
                    author = author,
                    state = enable,
                    component = component,
                    viewModel = viewModel,
                    lazyPagingItems = lazyPagingItems,
                    listState = listState,
                    engagement = engagement,
                    moderation = moderation,
                    onMentionClick = event::onMentionClick,
                    onHashtagClick = event::onHashtagClick,
                    onPostClick = event::onVideoClick,
                )
            }
        }
        LaunchedEffect(lastUpdated.value) {
            if (updatedAt.longValue != lastUpdated.value) {
                lazyPagingItems.refresh()
                updatedAt.longValue = lastUpdated.value
            }
        }
    }
}
