package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostAction(engagement: UiPost.Engagement) {
    PostMetric(
        text = engagement.likes,
        painter = painterResource(R.drawable.ic_love_outline),
        orientation = Orientation.Horizontal
    )
    PostMetric(
        text = engagement.dislikes,
        painter = painterResource(R.drawable.ic_hate_outline),
        orientation = Orientation.Horizontal
    )
    PostMetric(
        text = engagement.comment,
        painter = painterResource(R.drawable.ic_comment_outline),
        orientation = Orientation.Horizontal
    )
    PostMetric(
        text = engagement.views,
        painter = painterResource(R.drawable.ic_view),
        orientation = Orientation.Horizontal
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewPostAction() {
    DesignTheme(isDarkMode = false) {
        val model = UiPost.Engagement(
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
        ) { PostAction(model) }
    }
}
