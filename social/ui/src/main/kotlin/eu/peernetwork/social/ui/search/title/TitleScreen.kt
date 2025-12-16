package eu.peernetwork.social.ui.search.title

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
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
import eu.peernetwork.social.ui.model.UiPost
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun TitleScreen(
    query: TextFieldState,
    postLimit: Int,
    onClick: (UiPost) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Title.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TitleViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TitleViewModel.State.Empty -> DesignStreamState.Default
                TitleViewModel.State.Loading -> DesignStreamState.Loading
                is TitleViewModel.State.Success -> {
                    DesignStreamState.Success(
                        (state as TitleViewModel.State.Success).content
                    )
                }
                is TitleViewModel.State.Error -> DesignStreamState.Error(
                    (state as TitleViewModel.State.Error).error
                )
            }
        }
    }
    val handleOnClick by rememberUpdatedState(onClick)
    val lastSearch = rememberSaveable { mutableStateOf(query.text.toString()) }
    DesignPagingStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize()
            .then(modifier),
        loading = { TitleSkeleton(3) },
        error = { error ->
            TitleError(component.resource().error(error.value)) {
                viewModel.search(query.text.toString(), Pageable(0, postLimit))
            }
        }
    ) { lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { post ->
                    TitleItem(post.title) {
                        handleOnClick(post)
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
                    lastSearch.value = text
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
