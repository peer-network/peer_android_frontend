package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignOption
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    postId: State<String>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state = viewModel.state.collectAsStateWithLifecycle()
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
    LaunchedEffect(postId.value) {
        viewModel.load(postId.value, Pageable(0, postLimit))
    }
    DesignStatefulContent<Flow<PagingData<UiComment>>>(
        state = remember {
            derivedStateOf {
                when (state.value) {
                    CommentViewModel.State.Empty -> DesignStatefulContentState.Empty
                    CommentViewModel.State.Loading -> DesignStatefulContentState.Loading
                    is CommentViewModel.State.Success -> {
                        DesignStatefulContentState.Success(
                            (state.value as CommentViewModel.State.Success).content
                        )
                    }
                    is CommentViewModel.State.Error -> DesignStatefulContentState.Error(
                        (state.value as CommentViewModel.State.Error).error
                    )
                }
            }
        },
        onRefresh = { viewModel.load(postId.value, Pageable(0, postLimit)) }
    ) { flow ->
        val items = flow.collectAsLazyPagingItems()
        val commentState = remember { TextFieldState() }
        val focusRequester = remember { FocusRequester() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items.itemCount) { index ->
                items[index]?.let { comment ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(
                                text = comment.author.username,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = comment.content,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        DesignOption(
                            text = comment.likes.toString(),
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = "Action Icon",
                            tint = Color.Black,
                            onClick = {}
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DesignTextField(
                state = commentState,
                modifier = Modifier.weight(1f),
                focusRequester = focusRequester,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                onKeyboardAction = {
                    if (commentState.text.isNotBlank()) {
                        viewModel.comment(
                            postId.value,
                            commentState.text.toString()
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                lineLimits = TextFieldLineLimits.MultiLine(1, 3),
                placeholder = { Text("Write a comment...") }
            )

            IconButton(
                onClick = {
                    if (commentState.text.isNotBlank()) {
                        viewModel.comment(postId.value, commentState.text.toString())
                    }
                },
                enabled = commentState.text.isNotBlank()
                ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Send comment",
                    tint = if (commentState.text.isNotBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }
                )
            }
        }
    }
}