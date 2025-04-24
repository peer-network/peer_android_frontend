package eu.peernetwork.blog.ui.comment

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
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
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    id: String,
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onUpdate: () -> Unit,
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
                CommentViewModel.State.Idle -> DesignStatefulScaffoldState.Empty
                CommentViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is CommentViewModel.State.Content -> {
                    DesignStatefulScaffoldState.Success(
                        (sheetState.value as CommentViewModel.State.Content).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val isLoading = remember { derivedStateOf {
        (sheetState.value as? CommentViewModel.State.Content?)?.isLoading == true
    } }
    CommentScreen(
        id = id,
        state = derivedState,
        contentState = state,
        isLoading = isLoading,
        modifier = modifier.padding(horizontal = 24.dp)
            .fillMaxSize(),
        onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } },
        onUpdate = onUpdate
    ) { id, comment -> viewModel.comment(id, comment) }
    LaunchedEffect(sheetState.value) {
        val content = (sheetState.value as? CommentViewModel.State.Content?)
        if (content?.error != null && id == state.value?.id) {
            Toast.makeText(context, content.error.message, Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
    }
}

@Composable
fun CommentScreen(
    id: String,
    state: State<DesignStatefulScaffoldState>,
    contentState: MutableState<UiContent?>,
    isLoading: State<Boolean>,
    onRefresh: () -> Unit,
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
) {
    val comment = remember { TextFieldState() }
    val sheet = remember { mutableStateOf<UiContent?>(null) }
    CommentScaffold(
        tag = id,
        state = contentState,
        modifier = modifier,
        sheet = { sheet.value?.let {
            CommentForm(
                model = it,
                comment = comment,
                isLoading = isLoading,
                modifier = Modifier.padding(horizontal = 24.dp),
                onSubmit = onSubmit
            )
        } },
        content = { uiState ->
            DesignPagingScaffold<UiComment>(
                state = state,
                onRefresh = onRefresh,
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
                    item { Box(modifier = Modifier.navigationBarsPadding()
                        .padding(bottom = 200.dp)) }
                }
                LaunchedEffect(isLoading.value) {
                    if (!isLoading.value && comment.text.isNotEmpty()) {
                        comment.clearText()
                        items.refresh()
                        onUpdate()
                    }
                }
            }
            LaunchedEffect(uiState.value) {
                contentState.value?.let {
                    if (uiState.value) {
                        sheet.value = contentState.value
                        delay(50)
                        onRefresh()
                    }
                }
            }
        }
    )
}
