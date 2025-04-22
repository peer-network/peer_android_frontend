package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerAppRed

@Composable
fun EngagementScreen(
    engagement: UiEngagement,
    refresh: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onComment: () -> Unit,
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
            (state as? EngagementViewModel.State.Error?)
        }
    }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val post = remember(state) {
        derivedStateOf {
            if (state.engagements[engagement.id] != null) {
                state.engagements[engagement.id]!!
            } else {
                engagement
            }
        }
    }
    Row {
        PostIcon(
            action = UiAction.Like,
            value = post.value.likes.toString(),
            color = if (post.value.isLiked) {
                PeerAppRed
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { viewModel.like(engagement) }
        PostIcon(
            action = UiAction.Dislike,
            value = post.value.dislikes.toString(),
            color = if (post.value.isDisliked) {
                LightAccentColor
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { viewModel.dislike(post.value) }
        PostIcon(UiAction.Comment, post.value.comment.toString()) { onComment() }
    }
    LaunchedEffect(refresh.value) {
        if (refresh.value) {
            viewModel.reset()
            refresh.value = false
        }
    }
    LaunchedEffect(error.value) {
        if (error.value != null && error.value?.selected == engagement.id) {
            Toast.makeText(context, error.value?.error?.message ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clean()
        }
    }
}
