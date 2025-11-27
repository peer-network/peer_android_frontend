package eu.peernetwork.blog.ui.engagement.v2

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.mapper.v2.mapToEngagement
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.model.v2.UiEngagement
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.LightAccentColor

interface EngagementOption {
    fun observe(): androidx.compose.runtime.State<Map<String, UiReaction>>

    operator fun invoke(post: UiPost, state: State)

    sealed interface State {
        data object Like : State
        data object Dislike : State
        data object Comment : State
        data object View : State
    }
}

@Composable
fun EngagementOption(
    post: UiPost,
    state: State<Map<String, UiReaction>>,
    onClick: (EngagementOption.State) -> Unit
) {
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
    EngagementOption(
        onClick = onClick,
        engagement = engagement.value
    )
}

@Composable
fun EngagementOption(
    engagement: UiEngagement,
    onClick: (EngagementOption.State) -> Unit
) {
    val handleClick by rememberUpdatedState(onClick)
    EngagementMetric(
        text = engagement.likes,
        checked = engagement.isLiked,
        painter = painterResource(R.drawable.ic_love_outline),
        checkedPainter = painterResource(R.drawable.ic_love),
        orientation = Orientation.Horizontal
    ) { handleClick(EngagementOption.State.Like) }
    EngagementMetric(
        text = engagement.dislikes,
        checked = engagement.isDisliked,
        painter = painterResource(R.drawable.ic_hate_outline),
        checkedPainter = painterResource(R.drawable.ic_hate),
        checkedTint = LightAccentColor,
        orientation = Orientation.Horizontal
    ) { handleClick(EngagementOption.State.Dislike) }
    EngagementMetric(
        text = engagement.comment,
        painter = painterResource(R.drawable.ic_comment_outline),
        orientation = Orientation.Horizontal
    ) { handleClick(EngagementOption.State.Comment) }
    EngagementMetric(
        text = engagement.views,
        painter = painterResource(R.drawable.ic_view),
        orientation = Orientation.Horizontal
    ) { handleClick(EngagementOption.State.View) }
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 10.dp)
        ) { EngagementOption(model) {} }
    }
}
