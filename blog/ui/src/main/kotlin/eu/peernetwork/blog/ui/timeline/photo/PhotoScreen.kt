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
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun PhotoScreen(
    id: String,
    postLimit: Int,
    relation: Relation,
    criteria: Criteria? = null,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
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
    DesignPagingScaffold<UiPost>(
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
                    PhotoListing(
                        id = id,
                        component = component,
                        listState = listState,
                        lazyPagingItems = lazyPagingItems,
                        engagement = engagement,
                        moderation = moderation,
                        onPostClick = onPostClick,
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
        viewModel.load(Pageable(0, postLimit), relation, criteria)
    }
}
