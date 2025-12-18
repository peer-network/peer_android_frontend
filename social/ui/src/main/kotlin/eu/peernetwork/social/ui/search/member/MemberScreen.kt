package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
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
import eu.peernetwork.social.ui.model.UiMember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun MemberScreen(
    query: TextFieldState,
    postLimit: Int,
    onClick: (UiMember) -> Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = MemberViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val page = remember(postLimit) { Pageable(0, postLimit) }
    val handleClick by rememberUpdatedState(onClick)
    val updatedHeader by rememberUpdatedState(header)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                MemberViewModel.State.Empty -> DesignStreamState.Default
                MemberViewModel.State.Loading -> DesignStreamState.Loading
                is MemberViewModel.State.Success -> {
                    DesignStreamState.Success(
                        (state as MemberViewModel.State.Success).content
                    )
                }
                is MemberViewModel.State.Error -> DesignStreamState.Error(
                    (state as MemberViewModel.State.Error).error
                )
            }
        }
    }
    val lastSearch = remember {
        derivedStateOf {
            (state as? MemberViewModel.State.Success?)?.username
        }
    }
    DesignPagingStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize()
            .then(modifier),
        loading = { MemberSkeleton(3, header) },
        error = { error ->
            MemberError(component.resource().error(error.value)) {
                viewModel.search(query.text.toString(), page)
            }
        }
    ) { lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { updatedHeader() }
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { member ->
                    MemberItem(
                        slug = member.slug,
                        username = member.username,
                        imageUrl = member.imageUrl,
                        onClick = {
                            if (handleClick(member)) {
                                query.clearText()
                            }
                        }
                    )
                }
            }
        }
    }
    LaunchedEffect(query.text) {
        snapshotFlow { query.text.toString() }
            .debounce(300)
            .collectLatest { text ->
                if (text.length >= 3 && lastSearch.value != text) {
                    viewModel.search(text, page)
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
