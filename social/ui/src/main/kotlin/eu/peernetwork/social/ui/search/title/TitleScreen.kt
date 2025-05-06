package eu.peernetwork.social.ui.search.title

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulContentPlaceholder
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.model.UiPost
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun TitleScreen(
    query: TextFieldState,
    postLimit: Int,
    onClick: (String, String) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
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
                TitleViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                TitleViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is TitleViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as TitleViewModel.State.Success).content
                    )
                }
                is TitleViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as TitleViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold<UiPost>(
        state = derivedState,
        onRefresh = {
            if (query.text.length >= 3) {
                viewModel.search(query.text.toString(), Pageable(0, postLimit))
            }
        },
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp),
        placeholder = { DesignStatefulContentPlaceholder(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) }
    ) { state, lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { post ->
                    Box(modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(role = Role.Button) {
                            onClick(post.id, post.type)
                        }) {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
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
                if (text.length >= 3) {
                    viewModel.search(text, Pageable(0, postLimit))
                } else {
                    viewModel.reset()
                }
            }
    }
    DisposableEffect(query.text) {
        onDispose {
            viewModel.reset()
        }
    }
}
