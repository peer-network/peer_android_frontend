package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
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
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerAppRed
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EngagementScreen(
    postLimit: Int,
    refresh: State<Boolean>,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Engagements) -> Unit
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
    var post = remember { mutableStateOf<UiContent?>(null) }
    val scope = rememberCoroutineScope()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember { derivedStateOf { error.value != null } }
    val updatedContent by rememberUpdatedState(content)
    val handleMentionClick by rememberUpdatedState(onMentionClick)
    val handleHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    val type = remember { mutableStateOf<EngagementType?>(null) }
    val event = remember(state, reactionState.values) { Engagements(
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
        onLike = { type.value = EngagementType.Like(it.id) },
        onDisLike = { type.value = EngagementType.DisLike(it.id) },
        onComment = { post.value = it }
    ) }
    updatedContent(event)
    LaunchedEffect(Unit) { viewModel.initialize() }
    LaunchedEffect(refresh.value) {
        if (refresh.value) {
            viewModel.reset()
        }
    }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            val error = (state as? EngagementViewModel.State.Error)?.error?.message
            Toast.makeText(context, error?.let { component.resource().string(it) }
                ?: errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clear()
        }
    }
    CommentScreen(
        post,
        postLimit,
        component,
        viewModelStoreOwner,
        onMentionClick = {
            scope.launch {
                handleMentionClick(it)
            } },
        onHashtagClick = {
            scope.launch {
                handleHashtagClick(it)
            } },
        onAuthorClick = {
            scope.launch {
                handleAuthorClick(it)
            } }
    )
    component.engagementConfirmation()(
        Modifier,
        EngagementConfirmation.Spec(
            type,
            viewModelStoreOwner,
        ) {
            when(it) {
                is EngagementType.Like -> viewModel.like(it.id)
                is EngagementType.DisLike -> viewModel.dislike(it.id)
                else -> {}
            }
        }
    )
}

@Composable
fun EngagementScreen(
    model: UiContent,
    event: Engagements,
    vertical  : Boolean = false,           //  ← NEW, default keeps old behaviour
    modifier  : Modifier  = Modifier
) {
    val engagement by remember(model) { derivedStateOf { event.onLoad(model) } }
    val handleOnLike by rememberUpdatedState(event.onLike)
    val handleOnDisLike by rememberUpdatedState(event.onDisLike)
    val handleOnComment by rememberUpdatedState(event.onComment)
    val container: @Composable (@Composable () -> Unit) -> Unit =
        if (vertical) {
            { content -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement  = Arrangement.spacedBy(12.dp),
                modifier             = modifier
            ) { content() } }
        } else {
            { content -> Row(modifier = modifier) { content() } }
        }

    container {

        PostIcon(
            action = UiAction.Like,
            value  = engagement.likes.toString(),
            color  = if (engagement.isLiked) PeerAppRed else MaterialTheme.colorScheme.tertiary
        ) { handleOnLike(engagement) }

        PostIcon(
            action = UiAction.Dislike,
            value  = engagement.dislikes.toString(),
            color  = if (engagement.isDisliked) LightAccentColor else MaterialTheme.colorScheme.tertiary
        ) { handleOnDisLike(engagement) }

        PostIcon(
            action = UiAction.Comment,
            value  = engagement.comment.toString()
        ) { handleOnComment(model) }
    }
}
