package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignErrorLabel
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.compose.SearchItemSkeleton
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                MemberViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                MemberViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is MemberViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as MemberViewModel.State.Success).content
                    )
                }
                is MemberViewModel.State.Error -> DesignStatefulScaffoldState.Error(
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
    DesignPagingScaffold<UiMember>(
        state = derivedState,
        onRefresh = {
            if (query.text.length >= 3) {
                viewModel.search(query.text.toString(), page)
            }
        },
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp),
        placeholder = { SearchItemSkeleton(modifier = Modifier.padding(top = 16.dp)) },
        errorContent = { error, refresh ->
            Column(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(24.dp))
                DesignErrorLabel(refresh, error, component.resource())
            }
        }
    ) { state, lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item(key = "MemberListHeader") { Spacer(modifier = Modifier.height(24.dp)) }
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { member ->
                    if (member.imageUrl.isNotBlank()) {
                        MemberItem(
                            model = member,
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
    LaunchedEffect(Unit) {
        if (lastSearch.value != query.text.toString()) {
            viewModel.reset()
        }
    }
}
