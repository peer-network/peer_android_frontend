package eu.peernetwork.blog.ui.engagement.v2

import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.interaction.overview.v2.OverviewScreen
import eu.peernetwork.blog.ui.comment.v2.CommentScreen
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.engagement.EngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementObserver
import eu.peernetwork.blog.ui.engagement.EngagementViewModel
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun EngagementScreen(
    userId: String,
    postLimit: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (EngagementObserver) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Engagement.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = EngagementViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val reactionState = viewModel.reactions.collectAsStateWithLifecycle()
    val error = remember(state) {
        derivedStateOf {
            (state as? EngagementViewModel.State.Error?)?.error
        }
    }
    val post = remember { mutableStateOf<String?>(null) }
    val overview = remember { mutableStateOf<String?>(null) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember { derivedStateOf { error.value != null } }
    val updatedContent by rememberUpdatedState(content)
    val handleMentionClick by rememberUpdatedState(onMentionClick)
    val handleHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    val type = remember { mutableStateOf<EngagementEvent?>(null) }
    val observer = remember { object : EngagementObserver {
        override fun observe(): State<Map<String, UiReaction>> = reactionState

        override fun invoke(state: EngagementObserver.State) {
            if (state is EngagementObserver.State.Like) {
                type.value = EngagementEvent.Like(
                    post = state.id,
                    author = state.author,
                    message = state.message
                )
            } else if (state is EngagementObserver.State.Dislike) {
                type.value = EngagementEvent.DisLike(state.id)
            } else if (state is EngagementObserver.State.Comment) {
                post.value = state.id
            } else if (state is EngagementObserver.State.View) {
                overview.value = state.id
            }
        }
    } }
    updatedContent(observer)
    LaunchedEffect(Unit) { viewModel.initialize() }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            val error = (state as? EngagementViewModel.State.Error)?.error?.message
            Toast.makeText(context, error?.let { component.resource().string(it) }
                ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clear()
        }
    }
    CommentScreen(
        state = post,
        userId = userId,
        postLimit = postLimit,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        onMentionClick = { handleMentionClick(it) },
        onHashtagClick = { handleHashtagClick(it) },
        onAuthorClick = { handleAuthorClick(it) },
        connection = connection,
    )
    OverviewScreen(
        state = overview,
        postLimit = postLimit,
        provider = component,
        connection = connection,
        viewModelStoreOwner = viewModelStoreOwner,
        onAuthorClick = { handleAuthorClick(it) }
    )
    component.engagementConfirmation()(
        Modifier,
        spec = EngagementDialog.Spec(type, viewModelStoreOwner) {
            when(it) {
                is EngagementEvent.Like -> viewModel.like(
                    id = it.id,
                    author = it.author,
                    message = it.message
                )
                is EngagementEvent.DisLike -> viewModel.dislike(it.id)
                else -> {}
            }
        }
    )
}
