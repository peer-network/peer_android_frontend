package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.comment.CommentScreen
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerAppRed

data class EngagementSpec(
    val onInit: (UiContent) -> UiEngagement,
    val onLike: (UiEngagement) -> Unit,
    val onDisLike: (UiEngagement) -> Unit,
    val onComment: (UiContent) -> Unit,
)

@Composable
fun EngagementScreen(
    tag: String,
    postLimit: Int,
    refresh: State<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (EngagementSpec) -> Unit
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
    val error = remember(state) {
        derivedStateOf {
            (state as? EngagementViewModel.State.Content?)?.error
        }
    }
    var post = remember { mutableStateOf<UiContent?>(null) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember {
        derivedStateOf { error.value != null }
    }
    val isRefreshed = remember {
        derivedStateOf {
            (state as? EngagementViewModel.State.Content?)
                ?.engagements?.isEmpty() == false && refresh.value
        }
    }
    content(EngagementSpec(
        onInit = {
            (state as? EngagementViewModel.State.Content?)
                ?.engagements?.get(it.id) ?: it.mapToEngagement()
        },
        onLike = { viewModel.like(it) },
        onDisLike = { viewModel.dislike(it) },
        onComment = { post.value = it }
    ))
    LaunchedEffect(isRefreshed.value) {
        if (isRefreshed.value) {
            viewModel.reset()
        }
    }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            Toast.makeText(context, error.value?.message ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clear()
        }
    }
    CommentScreen(tag, post, postLimit, component, viewModelStoreOwner) {
        post.value?.mapToEngagement()?.let {
            val engagement = (state as? EngagementViewModel.State.Content?)
                ?.engagements?.get(it.id) ?: it
            viewModel.comment(engagement)
        }
    }
}

@Composable
fun EngagementScreen(
    model: UiContent,
    spec: EngagementSpec,
) {
    val engagement by remember(model) { derivedStateOf { spec.onInit(model) } }
    Row {
        PostIcon(
            action = UiAction.Like,
            value = engagement.likes.toString(),
            color = if (engagement.isLiked) {
                PeerAppRed
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { spec.onLike(engagement) }
        PostIcon(
            action = UiAction.Dislike,
            value = engagement.dislikes.toString(),
            color = if (engagement.isDisliked) {
                LightAccentColor
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { spec.onDisLike(engagement) }
        PostIcon(UiAction.Comment, engagement.comment.toString()) {
            spec.onComment(model)
        }
    }
}
