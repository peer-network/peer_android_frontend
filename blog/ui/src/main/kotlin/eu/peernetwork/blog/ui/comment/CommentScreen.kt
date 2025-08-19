package eu.peernetwork.blog.ui.comment

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.compose.Placeholder
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val sheetState = viewModel.state.collectAsStateWithLifecycle()
    val sheetStatus = viewModel.status.collectAsStateWithLifecycle()
    val likesState = viewModel.likes.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (sheetState.value) {
                is CommentViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                is CommentViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is CommentViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (sheetState.value as CommentViewModel.State.Success).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val isLoading = remember {
        derivedStateOf {
            sheetStatus.value is CommentViewModel.Status.Loading
        }
    }
    val error = remember {
        derivedStateOf {
            (sheetStatus.value as? CommentViewModel.Status.Error?)
                ?.error?.message
        }
    }
    val isCommentPosted = remember {
        derivedStateOf {
            (sheetStatus.value as? CommentViewModel.Status.Success<*>?)
                ?.intent == CommentViewModel.Intent.Comment
        }
    }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val handleOnMentionClick by rememberUpdatedState(onMentionClick)
    val handleOnHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleOnAuthorClick by rememberUpdatedState(onAuthorClick)
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    CommentScreen(
        state = state,
        replyTo = replyTo,
        isLoading = isLoading,
        onDismiss = {
            action.value?.invoke()
            action.value = null },
        onMentionClick = { username ->
            action.value = { handleOnMentionClick(username) }
            state.value = null
        },
        onHashtagClick = { hashtag ->
            action.value = { handleOnHashtagClick(hashtag) }
            state.value = null
        },
        onSubmit = { id, comment -> viewModel.comment(id, comment) }
    ) { size, field ->
        DesignPagingScaffold(
            state = derivedState,
            onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } },
            placeholder = { Placeholder(modifier = Modifier.padding(horizontal = 24.dp)) }
        ) { pageState, items ->
            CommentListing(
                likesState,
                items,
                size = size.value,
                onLike = { viewModel.like(it) },
                { replyTo.value = it },
                onMentionClick = { username ->
                    action.value = { handleOnMentionClick(username) }
                    state.value = null
                },
                onHashtagClick = { hashtag ->
                    action.value = { handleOnHashtagClick(hashtag) }
                    state.value = null
                },
                onAuthorClick = {
                    action.value = { handleOnAuthorClick(it) }
                    state.value = null
                },
            )
            LaunchedEffect(isLoading.value) {
                if (isCommentPosted.value) {
                    field.clearText()
                    items.refresh()
                }
            }
        }
        LaunchedEffect(error.value) {
            if (error.value != null) {
                Toast.makeText(
                    context,
                    error.value?.let { component.resource().string(it) },
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clear()
            }
        }
        LaunchedEffect(state.value) {
            if (state.value != null) {
                state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) }
            } else {
                viewModel.reset()
            }
        }
    }
}

@Composable
fun CommentScreen(
    state: MutableState<UiContent?>,
    replyTo: MutableState<String?>,
    isLoading: State<Boolean>,
    onDismiss: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onSubmit: (String, String) -> Unit = { id, comment -> },
    content: @Composable (State<Size>, TextFieldState) -> Unit = { visible, field -> }
) {
    val comment = remember { TextFieldState() }
    val updatedContent by rememberUpdatedState(content)
    CommentScaffold(
        state = state,
        onDismiss = onDismiss,
        sheet = {
            val content = remember { mutableStateOf(state.value) }
            content.value?.let {
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
        content = { updatedContent(it, comment) }
    )
}
