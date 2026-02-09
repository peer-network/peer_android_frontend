package eu.peernetwork.social.ui.search.tag

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun TagScreen(
    query: TextFieldState,
    postLimit: Int,
    onClick: (String) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Tag.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TagViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val handleClick by rememberUpdatedState(onClick)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TagViewModel.State.Empty -> DesignStreamState.Default
                TagViewModel.State.Loading -> DesignStreamState.Loading
                is TagViewModel.State.Success -> {
                    DesignStreamState.Success(
                        (state as TagViewModel.State.Success).content
                    )
                }
                is TagViewModel.State.Error -> DesignStreamState.Error(
                    (state as TagViewModel.State.Error).error
                )
            }
        }
    }
    val lastSearch = remember {
        derivedStateOf {
            (state as? TagViewModel.State.Success?)?.tag
        }
    }
    DesignPagingStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize()
            .then(modifier),
        loading = { TagSkeleton(3) },
        error = { error ->
            TagError(component.resource().error(error.value)) {
                viewModel.search(query.text.toString(), Pageable(0, postLimit))
            }
        }
    ) { lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { tag ->
                    TagItem(tag.value) {
                        handleClick(tag.value)
                    }
                }
            }
        }
    }
    LaunchedEffect(query.text) {
        snapshotFlow { query.text.toString() }
            .debounce(300)
            .collectLatest { text ->
                if (text.length >= 3 && lastSearch.value != text) {
                    viewModel.search(text, Pageable(0, postLimit))
                } else if (lastSearch.value != text) {
                    viewModel.reset()
                }
            }
    }
    DisposableEffect(Unit) {
        onDispose {
            if (lastSearch.value != query.text.toString()) {
                viewModel.reset()
            }
        }
    }
}
