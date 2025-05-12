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
import eu.peernetwork.blog.ui.compose.ContentBar
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
    tag: String,
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {}
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
                CommentViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is CommentViewModel.State.Content -> {
                    DesignStatefulScaffoldState.Success(
                        (sheetState.value as CommentViewModel.State.Content).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
                else -> DesignStatefulScaffoldState.Empty
            }
        }
    }
    val contents = remember { derivedStateOf {
        sheetState.value as? CommentViewModel.State.Content?
    } }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { derivedStateOf { contents.value?.isLoading == true } }
    val isSelected = remember { derivedStateOf { contents.value?.selected != null } }
    CommentScreen(
        tag = tag,
        state = state,
        replyTo = replyTo,
        isLoading = isLoading,
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        modifier = modifier.fillMaxSize(),
        onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } },
        onSubmit = { id, comment -> viewModel.comment(id, comment) }
    ) {
        DesignPagingScaffold<UiComment>(
            state = derivedState,
            onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } },
            placeholder = { ContentSkeleton(modifier = Modifier.padding(horizontal = 24.dp)) }
        ) { pageState, items ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items.itemCount) { index ->
                    items[index]?.let { comment ->
                        ContentBar(
                            model = comment.mapToContent(),
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 12.dp),
                            titleOnClick = {
                                replyTo.value = comment.author.username
                            },
                            onMentionClick = onMentionClick,
                            onHashtagClick = onHashtagClick
                        ) {
                            val liked = remember { derivedStateOf {
                                contents.value?.likes?.firstOrNull { it.id == comment.id }
                            } }
                            CommentOptions(
                                likes = liked.value?.likes ?: comment.likes,
                                isLiked = liked.value?.isLiked ?: comment.isLiked
                            ) {
                                viewModel.like(comment)
                                items.refresh()
                            }
                        }
                    }
                }
                item { Box(modifier = Modifier.navigationBarsPadding()
                    .padding(bottom = 200.dp)) }
            }
            LaunchedEffect(isLoading.value) {
                if (!isLoading.value && isSelected.value) {
                    it.clearText()
                    items.refresh()
                }
            }
        }
    }
    LaunchedEffect(sheetState.value) {
        val content = (sheetState.value as? CommentViewModel.State.Content?)
        if (content?.error != null) {
            Toast.makeText(context, content.error.message, Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
    }
}

@Composable
fun CommentScreen(
    tag: String,
    state: MutableState<UiContent?>,
    replyTo: MutableState<String?>,
    isLoading: State<Boolean>,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onSubmit: (String, String) -> Unit = { id, comment -> },
    content: @Composable (TextFieldState) -> Unit = {}
) {
    val comment = remember { TextFieldState() }
    val sheet = remember { mutableStateOf<UiContent?>(null) }
    CommentScaffold(
        tag = tag,
        state = state,
        modifier = modifier,
        sheet = { sheet.value?.let {
            CommentForm(
                model = it,
                comment = comment,
                replyTo = replyTo,
                isLoading = isLoading,
                modifier = Modifier.padding(horizontal = 24.dp),
                onSubmit = onSubmit,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        } },
        content = { uiState ->
            content(comment)
            LaunchedEffect(uiState.value) {
                state.value?.let {
                    if (uiState.value) {
                        sheet.value = state.value
                        delay(50)
                        onRefresh()
                    }
                }
            }
        }
    )
}
