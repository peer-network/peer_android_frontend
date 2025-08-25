package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun PhotoScreen(
    id: String,
    postLimit: Int,
    category: Category,
    criteria: Criteria? = null,
    event: UiPostEvent,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    requireUpdate: MutableState<Boolean>,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val coroutine = rememberCoroutineScope()
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error.let {
                        Throwable(component.resource().string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    DesignPagingScaffold(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit), category, criteria) },
        placeholder = { PostPlaceholder() },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { state, lazyPagingItems ->
        val refreshState = remember { derivedStateOf {
            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    DesignStatefulScaffoldState.Loading
                }
                is LoadState.Error -> {
                    DesignStatefulScaffoldState.Error(
                        (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    )
                }
                else -> state.value
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
                event::onMentionClick,
                event::onHashtagClick,
                event::onAuthorClick,
                component,
                viewModelStoreOwner,
                connection
            ) { engagement ->
                ModerationScreen(
                    component,
                    viewModelStoreOwner
                ) { moderation ->
                    PhotoListing(
                        id = id,
                        viewModel = viewModel,
                        component = component,
                        listState = listState,
                        lazyPagingItems = lazyPagingItems,
                        engagement = engagement,
                        moderation = moderation,
                        onPostClick = event::onPostClick,
                        onAuthorClick = event::onAuthorClick,
                        onHashtagClick = event::onHashtagClick,
                        onMentionClick = event::onMentionClick,
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
    }
    LaunchedEffect(category, criteria) {
        if (category != viewModel.lastCategory) {
            viewModel.load(Pageable(0, postLimit), category, criteria)
        }
    }
}
