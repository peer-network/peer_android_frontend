package eu.peernetwork.messaging.ui.chat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.PhotoPlaceholder
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.blog.ui.explore.ExploreListing
import eu.peernetwork.blog.ui.explore.ExploreViewModel

@Composable
fun ExplorerOnHomeScreen(
    modifier: Modifier = Modifier,
    postLimit: Int = 60,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (UiPost, Int) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(eu.peernetwork.blog.ui.explore.Explore.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ExploreViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)

    // CRITICAL: fetch posts on first render
    LaunchedEffect(Unit) {
        viewModel.get(Pageable(0, postLimit))
    }

    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ExploreViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ExploreViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ExploreViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as ExploreViewModel.State.Success).content
                )
                is ExploreViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as ExploreViewModel.State.Error).error.let {
                        Throwable(component.resource().string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    DesignPagingScaffold(
        state = derivedState,
        onRefresh = { viewModel.get(Pageable(0, postLimit)) },
        placeholder = { PhotoPlaceholder(modifier = Modifier.padding(top = 72.dp)) },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        },
        modifier = modifier
    ) { state, lazyPagingItems ->
        val refreshState = remember {
            derivedStateOf {
                when (lazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> DesignStatefulScaffoldState.Loading
                    is LoadState.Error -> DesignStatefulScaffoldState.Error(
                        (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    )
                    else -> state.value
                }
            }
        }
        ExplorerOnHomeScaffold {
            DesignRefreshableScaffold<LazyPagingItems<UiPost>>(
                state = refreshState,
                onRefresh = { lazyPagingItems.refresh() }
            ) {
                ExploreListing(component, lazyPagingItems, listState, onClick)
            }
        }
    }
}

@Composable
fun ExplorerOnHomeScaffold(content: @Composable () -> Unit) {
    val updatedContent by rememberUpdatedState(content)
    DesignScaffold(
        alwaysReturn = true,
        header = { Spacer(modifier = Modifier.height(64.dp)) },
        footer = {  },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { _ -> updatedContent() }
}