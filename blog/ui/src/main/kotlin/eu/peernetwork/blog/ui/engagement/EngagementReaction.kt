package eu.peernetwork.blog.ui.engagement

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.LightAccentColor

interface EngagementReaction {
    operator fun invoke(post: UiPost, state: State)

    sealed interface State {
        data object Like : State
        data object Dislike : State
        data object Comment : State
        data object View : State
    }

    companion object {
        val LocalEngagementReaction = staticCompositionLocalOf<EngagementReaction> {
            error("EngagementReaction not provided")
        }
    }
}

@Composable
fun EngagementReaction(
    post: UiPost,
    size: Dp = 20.dp,
    orientation: Orientation = Orientation.Horizontal,
    state: State<Map<String, UiReaction>>,
    onClick: (EngagementReaction.State) -> Unit
) {
    EngagementReactionStream(
        post = post,
        state = state,
    ) { engagement ->
        EngagementReaction(
            size = size,
            onClick = onClick,
            engagement = engagement.value,
            orientation = orientation,
        )
    }
}

@Composable
fun EngagementReactionStream(
    post: UiPost,
    state: State<Map<String, UiReaction>>,
    content: @Composable (State<UiEngagement>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val isLiked = state.value[post.id]?.isLiked
    val isDisliked = state.value[post.id]?.isDisliked
    val isViewed = state.value[post.id]?.isViewed
    val commented = state.value[post.id]?.commented ?: 0
    val likeCount = post.likes + (isLiked == true && !post.isLiked).toInt()
    val dislikeCount = post.dislikes + (isDisliked == true && !post.isDisliked).toInt()
    val viewCount = post.views + (isViewed == true && !post.isViewed).toInt()
    val engagement = remember(state.value) { derivedStateOf {
        post.mapToEngagement().copy(
            likes = likeCount.toString(),
            isLiked = isLiked ?: post.isLiked,
            dislikes = dislikeCount.toString(),
            isDisliked = isDisliked ?: post.isDisliked,
            comment = (post.comment + commented).toString(),
            views = viewCount.toString()
        )
    } }
    updatedContent(engagement)
}

@Composable
fun EngagementReaction(
    engagement: UiEngagement,
    size: Dp = 20.dp,
    orientation: Orientation = Orientation.Horizontal,
    onClick: (EngagementReaction.State) -> Unit
) {
    val handleClick by rememberUpdatedState(onClick)
    EngagementMetric(
        size = size,
        text = engagement.likes,
        checked = engagement.isLiked,
        painter = painterResource(R.drawable.ic_love_outline),
        checkedPainter = painterResource(R.drawable.ic_love),
        orientation = orientation
    ) { handleClick(EngagementReaction.State.Like) }
    EngagementMetric(
        size = size,
        text = engagement.dislikes,
        checked = engagement.isDisliked,
        painter = painterResource(R.drawable.ic_hate_outline),
        checkedPainter = painterResource(R.drawable.ic_hate),
        checkedTint = LightAccentColor,
        orientation = orientation
    ) { handleClick(EngagementReaction.State.Dislike) }
    EngagementMetric(
        size = size,
        text = engagement.comment,
        painter = painterResource(R.drawable.ic_comment_outline),
        orientation = orientation
    ) { handleClick(EngagementReaction.State.Comment) }
    EngagementMetric(
        size = size,
        text = engagement.views,
        painter = painterResource(R.drawable.ic_view),
        orientation = orientation
    ) { handleClick(EngagementReaction.State.View) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewEngagementOption() {
    DesignTheme(isDarkMode = false) {
        val model = UiEngagement(
            id = "<test-id>",
            likes = "5k",
            dislikes = "1k",
            isDisliked = false,
            isLiked = false,
            views = "3k",
            comment = "1k"
        )
        Column {
            EngagementReaction(
                size = 24.dp,
                engagement = model,
                orientation = Orientation.Vertical
            ) {}
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { EngagementReaction(model) {} }
        }
    }
}
