package eu.peernetwork.blog.ui.engagement

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.interaction.overview.OverviewScreen
import eu.peernetwork.blog.ui.comment.CommentScreen
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerAppRed

@Composable
fun EngagementScreen(
    postLimit: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (UiEngagementEvent) -> Unit
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
    val post = remember { mutableStateOf<UiContent?>(null) }
    val overview = remember { mutableStateOf<UiContent?>(null) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val hasError = remember { derivedStateOf { error.value != null } }
    val updatedContent by rememberUpdatedState(content)
    val handleMentionClick by rememberUpdatedState(onMentionClick)
    val handleHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    val type = remember { mutableStateOf<EngagementEvent?>(null) }
    val event = remember(state) {
        UiEngagementEvent(
            onLoad = {
                val isLiked = reactionState[it.id]?.isLiked
                val isDisliked = reactionState[it.id]?.isDisliked
                val isViewed = reactionState[it.id]?.isViewed
                val commented = reactionState[it.id]?.commented ?: 0
                val likeCount = it.likes + (isLiked == true && !it.isLiked).toInt()
                val dislikeCount = it.dislikes + (isDisliked == true && !it.isDisliked).toInt()
                val viewCount = it.views + (isViewed == true && !it.isViewed).toInt()
                it.mapToEngagement().copy(
                    likes = likeCount,
                    isLiked = isLiked ?: it.isLiked,
                    dislikes = dislikeCount,
                    isDisliked = isDisliked ?: it.isDisliked,
                    comment = it.comment + commented,
                    views = viewCount
                )
            },
            onLike = { type.value = EngagementEvent.Like(it) },
            onDisLike = { type.value = EngagementEvent.DisLike(it.id) },
            onComment = { post.value = it },
            onView = {
                val isLiked = reactionState[it.id]?.isLiked
                val isDisliked = reactionState[it.id]?.isDisliked
                val isViewed = reactionState[it.id]?.isViewed
                val commented = reactionState[it.id]?.commented ?: 0
                val likeCount = it.likes + (isLiked == true && !it.isLiked).toInt()
                val dislikeCount = it.dislikes + (isDisliked == true && !it.isDisliked).toInt()
                val viewCount = it.views + (isViewed == true && !it.isViewed).toInt()
                overview.value = it.copy(
                    likes = likeCount,
                    isLiked = isLiked ?: it.isLiked,
                    dislikes = dislikeCount,
                    isDisliked = isDisliked ?: it.isDisliked,
                    comment = it.comment + commented,
                    views = viewCount
                )
            }
        )
    }
    updatedContent(event)
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
        post,
        postLimit,
        component,
        viewModelStoreOwner,
        onMentionClick = { handleMentionClick(it) },
        onHashtagClick = { handleHashtagClick(it) },
        onAuthorClick = { handleAuthorClick(it) }
    )
    OverviewScreen(
        state = overview,
        postLimit = postLimit,
        provider = component,
        connection = connection,
        onAuthorClick = { handleAuthorClick(it) }
    )
    component.engagementConfirmation()(
        Modifier,
        EngagementDialog.Spec(
            type,
            viewModelStoreOwner,
        ) {
            when(it) {
                is EngagementEvent.Like -> viewModel.like(it.content)
                is EngagementEvent.DisLike -> viewModel.dislike(it.id)
                else -> {}
            }
        }
    )
}

@Composable
fun EngagementScreen(
    model: UiContent,
    event: UiEngagementEvent,
    size: Dp = 28.dp,
    spacer: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.tertiary,
    padding: PaddingValues = PaddingValues(2.dp),
    orientation: Orientation = Orientation.Horizontal,
) {
    val engagement by remember(model) { derivedStateOf { event.onLoad(model) } }
    if (orientation == Orientation.Horizontal) {
        Row {
            PostIcon(
                action = UiAction.Like,
                value = engagement.likes.toString(),
                color = if (engagement.isLiked) {
                    PeerAppRed
                } else {
                    color
                },
                size = size,
                padding = padding,
                orientation = orientation
            ) {
                if (!engagement.isLiked) {
                    event.onLike(model)
                }
            }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                action = UiAction.Dislike,
                value = engagement.dislikes.toString(),
                color = if (engagement.isDisliked) {
                    LightAccentColor
                } else {
                    color
                },
                size = size,
                padding = padding,
                orientation = orientation
            ) {
                if (!engagement.isDisliked) {
                    event.onDisLike(model)
                }
            }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                UiAction.Comment,
                engagement.comment.toString(),
                size = size,
                padding = padding,
                orientation = orientation
            ) { event.onComment(model) }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                action = UiAction.View,
                value = engagement.views.toString(),
                color = color,
                size = size,
                padding = padding,
                orientation = orientation
            ) { event.onView(model) }
        }
    } else {
        Column {
            PostIcon(
                action = UiAction.Like,
                value = engagement.likes.toString(),
                color = if (engagement.isLiked) {
                    PeerAppRed
                } else {
                    color
                },
                size = size,
                padding = padding,
                orientation = orientation
            ) {
                if (!engagement.isLiked) {
                    event.onLike(model)
                }
            }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                action = UiAction.Dislike,
                value = engagement.dislikes.toString(),
                color = if (engagement.isDisliked) {
                    LightAccentColor
                } else {
                    color
                },
                size = size,
                padding = padding,
                orientation = orientation
            ) {
                if (!engagement.isDisliked) {
                    event.onDisLike(model)
                }
            }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                UiAction.Comment,
                engagement.comment.toString(),
                color = color,
                size = size,
                padding = padding,
                orientation = orientation
            ) { event.onComment(model) }
            Spacer(modifier = Modifier.size(spacer))
            PostIcon(
                action = UiAction.View,
                value = engagement.views.toString(),
                color = color,
                size = size,
                padding = padding,
                orientation = orientation
            ) { event.onView(model) }
        }
    }
}
