package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignError
import eu.peernetwork.core.ui.design.compose.DesignPagingScaffold
import eu.peernetwork.core.ui.design.compose.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ExploreScreen(
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (UiPost, Int) -> Unit
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)
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
        placeholder = {  },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        },
        modifier = modifier
    ) { state, lazyPagingItems ->
        val refreshState = remember {
            derivedStateOf {
                if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                    DesignStatefulScaffoldState.Loading
                } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                    DesignStatefulScaffoldState.Error(
                        (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    )
                } else {
                    state.value
                }
            }
        }
        ExploreScreen {
            DesignRefreshableScaffold<LazyPagingItems<UiPost>>(
                state = refreshState,
                onRefresh = { lazyPagingItems.refresh() }
            ) { ExploreListing(component, lazyPagingItems, listState, onClick) }
        }
    }
}

@Composable
fun ExploreScreen(content: @Composable () -> Unit) {
    val updatedContent by rememberUpdatedState(content)
    DesignScaffold(
        alwaysReturn = true,
        header = { Spacer(modifier = Modifier.height(64.dp)) },
        footer = {  },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { state -> updatedContent() }
}
