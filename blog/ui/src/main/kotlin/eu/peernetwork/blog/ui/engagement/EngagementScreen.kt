package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import eu.peernetwork.blog.ui.comment.CommentSheet
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.interaction.overview.OverviewScreen
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiPostDetail
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun EngagementScreen(
    username: String,
    imageUrl: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable () -> Unit
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
    val post = remember { mutableStateOf<UiPostDetail?>(null) }
    val overview = remember { mutableStateOf<UiEngagement?>(null) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember { derivedStateOf { error.value != null } }
    val updatedContent by rememberUpdatedState(content)
    val type = remember { mutableStateOf<EngagementIntent?>(null) }
    val interactor = remember { object : EngagementInteractor {
        override fun observe(): State<Map<String, UiReaction>> = reactionState

        override fun invoke(state: EngagementInteractor.State) {
            if (state is EngagementInteractor.State.Like) {
                type.value = EngagementIntent.Like(
                    post = state.id,
                    author = state.author,
                    message = state.message
                )
            } else if (state is EngagementInteractor.State.Dislike) {
                type.value = EngagementIntent.DisLike(state.id)
            } else if (state is EngagementInteractor.State.Comment) {
                post.value = state.model
            } else if (state is EngagementInteractor.State.View) {
                overview.value = state.engagement
            }
        }
    } }
    CompositionLocalProvider(LocalEngagementInteractor provides interactor) {
        updatedContent()
    }
    LaunchedEffect(Unit) { viewModel.initialize() }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            val error = (state as? EngagementViewModel.State.Error)?.error?.message
            Toast.makeText(context, error?.let { component.resource().string(it) }
                ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clear()
        }
    }
    CommentSheet(
        username = username,
        imageUrl = imageUrl,
        state = post,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    )
    OverviewScreen(
        state = overview,
        postLimit = limit,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) {}
    component.engagementConfirmation()(
        Modifier,
        spec = EngagementDialog.Spec(type, viewModelStoreOwner) {
            when(it) {
                is EngagementIntent.Like -> viewModel.like(
                    id = it.id,
                    author = it.author,
                    message = it.message
                )
                is EngagementIntent.DisLike -> viewModel.dislike(it.id)
                else -> {}
            }
        }
    )
}
