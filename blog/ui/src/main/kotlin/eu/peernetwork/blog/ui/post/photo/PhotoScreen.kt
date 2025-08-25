package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PhotoScreen(
    author: String,
    postLimit: Int,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
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
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as PhotoViewModel.State.Success).content
                    )
                }
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold(
        state = derivedState,
        placeholder = { PostPlaceholder() },
        onRefresh = { viewModel.load(author, Pageable(0, postLimit)) },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { state, lazyPagingItems ->
        val refreshed = remember { derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        } }
        EngagementScreen(
            postLimit,
            refreshed,
            onMentionClick = event::onMentionClick,
            onHashtagClick = event::onHashtagClick,
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
                    author = author,
                    viewModel = viewModel,
                    component = component,
                    lazyPagingItems = lazyPagingItems,
                    listState = listState,
                    engagement = engagement,
                    moderation = moderation,
                    onMentionClick = event::onMentionClick,
                    onHashtagClick = event::onHashtagClick,
                    onPostClick = event::onPostClick,
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
