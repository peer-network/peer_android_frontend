package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerAppRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class EngagementSpec(
    val onLoad: (UiContent) -> UiEngagement,
    val onLike: (UiEngagement) -> Unit,
    val onDisLike: (UiEngagement) -> Unit,
    val onComment: (UiContent) -> Unit,
)

@Composable
fun EngagementScreen(
    postLimit: Int,
    refresh: State<Boolean>,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    imageOnClick: (String) -> Unit = {},
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
    val reactionState by viewModel.reactions.collectAsStateWithLifecycle()
    val error = remember(state) {
        derivedStateOf {
            (state as? EngagementViewModel.State.Error?)?.error
        }
    }
    val tag = remember { System.currentTimeMillis().toString() }
    var post = remember { mutableStateOf<UiContent?>(null) }
    val scope = rememberCoroutineScope()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember { derivedStateOf { error.value != null } }
    val updatedContent by rememberUpdatedState(content)
    val handleMentionClick by rememberUpdatedState(onMentionClick)
    val handleHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleImageClick by rememberUpdatedState(imageOnClick)
    val spec = remember(state, reactionState.values) { EngagementSpec(
        onLoad = {
            val isLiked = reactionState[it.id]?.isLiked
            val isDisliked = reactionState[it.id]?.isDisliked
            val commented = reactionState[it.id]?.commented ?: 0
            it.mapToEngagement().copy(
                likes = it.likes + (isLiked == true && !it.isLiked).toInt(),
                isLiked = isLiked ?: it.isLiked,
                dislikes = it.dislikes + (isDisliked == true && !it.isDisliked).toInt(),
                isDisliked = isDisliked ?: it.isDisliked,
                comment = it.comment + commented
            )
        },
        onLike = { viewModel.like(it.id) },
        onDisLike = { viewModel.dislike(it.id) },
        onComment = { post.value = it }
    ) }
    updatedContent(spec)
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    LaunchedEffect(refresh.value) {
        if (refresh.value) {
            viewModel.reset()
        }
    }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            val error = (state as? EngagementViewModel.State.Error)?.error?.message
            Toast.makeText(context, error?.let { component.resource().string(it) } ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clear()
        }
    }
    CommentScreen(
        tag,
        post,
        postLimit,
        component,
        viewModelStoreOwner,
        onMentionClick = {
            scope.launch {
                post.value = null
                delay(DefaultDurationMillis.toLong())
                handleMentionClick(it)
            } },
        onHashtagClick = {
            scope.launch {
                post.value = null
                delay(DefaultDurationMillis.toLong())
                handleHashtagClick(it)
            } },
        imageOnClick = {
            scope.launch {
                post.value = null
                delay(DefaultDurationMillis.toLong())
                handleImageClick(it)
            } }
    )
}

@Composable
fun EngagementScreen(
    model: UiContent,
    spec: EngagementSpec,
) {
    val engagement by remember(model) { derivedStateOf { spec.onLoad(model) } }
    val handleOnLike by rememberUpdatedState(spec.onLike)
    val handleOnDisLike by rememberUpdatedState(spec.onDisLike)
    val handleOnComment by rememberUpdatedState(spec.onComment)
    Row {
        PostIcon(
            action = UiAction.Like,
            value = engagement.likes.toString(),
            color = if (engagement.isLiked) {
                PeerAppRed
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { handleOnLike(engagement) }
        PostIcon(
            action = UiAction.Dislike,
            value = engagement.dislikes.toString(),
            color = if (engagement.isDisliked) {
                LightAccentColor
            } else {
                MaterialTheme.colorScheme.tertiary
            },
        ) { handleOnDisLike(engagement) }
        PostIcon(UiAction.Comment, engagement.comment.toString()) {
            handleOnComment(model)
        }
    }
}
