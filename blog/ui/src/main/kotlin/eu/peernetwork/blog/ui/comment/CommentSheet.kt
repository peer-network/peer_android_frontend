package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.compose.ContentBadge
import eu.peernetwork.blog.ui.compose.ContentSkeleton
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val sheetState = viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (sheetState.value) {
                CommentViewModel.State.Empty -> DesignStatefulContentState.Empty
                CommentViewModel.State.Loading -> DesignStatefulContentState.Loading
                is CommentViewModel.State.Success -> {
                    DesignStatefulContentState.Success(
                        (sheetState.value as CommentViewModel.State.Success).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulContentState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    DesignBottomSheet(
        showSheet = showSheet,
        tag = "commentBottomSheet",
        modifier = modifier,
        onDismissRequest = { state.value = null },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        background = {
            DesignOverlayBackground(
                state = it,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        },
        content = { contentState ->
            CommentScaffold(
                modifier = Modifier.statusBarsPadding(),
                header = {
                    state.value?.let {
                        ContentBadge(
                            model = it,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {}
                    }
                }
            ) {
                CommentSheetContent(
                    state = derivedState,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } }
                )
            }
            LaunchedEffect(contentState.value) {
                state.value?.let {
                    if (contentState.value) {
                        delay(100)
                        viewModel.load(it.id, Pageable(0, postLimit))
                    }
                }
            }
        }
    )
}

@Composable
fun CommentSheetContent(
    state: State<DesignStatefulContentState>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignPagingContent<UiComment>(
        state = state,
        onRefresh = onRefresh,
        modifier = modifier,
        placeholder = { ContentSkeleton() }
    ) { pageState, items ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items.itemCount) { index ->
                items[index]?.let { comment ->
                    ContentBadge(
                        model = comment.mapToContent(),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {}
                }
            }
            item { Box(modifier = Modifier.height(64.dp)) }
        }
    }
}
