package eu.peernetwork.blog.ui.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ExploreScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Explore.Component, ExploreViewModel) -> Unit
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
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
fun ExploreScreen(
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Explore.Component, LazyPagingItems<UiPost>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    ExploreScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val isLoading = remember { derivedStateOf { state == ExploreViewModel.State.Loading  } }
        val isRefreshing = remember { mutableStateOf(isLoading.value) }
        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    ExploreViewModel.State.Empty -> DesignStreamState.Default
                    ExploreViewModel.State.Loading -> DesignStreamState.Loading
                    is ExploreViewModel.State.Success -> DesignStreamState.Success(
                        (state as ExploreViewModel.State.Success).content
                    )
                    is ExploreViewModel.State.Error -> DesignStreamState.Error(
                        (state as ExploreViewModel.State.Error).error
                    )
                }
            }
        }
        DesignRefreshScaffold(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.get(Pageable(0, limit)) }
        ) {
            DesignPagingStream(
                state = derivedState,
                loading = {  }
            ) { lazyPagingItems ->
                updatedContent(component, lazyPagingItems)
            }
        }
        LaunchedEffect(Unit)  {
            if (state is ExploreViewModel.State.Empty) {
                viewModel.get(Pageable(0, limit))
            }
        }
    }
}
