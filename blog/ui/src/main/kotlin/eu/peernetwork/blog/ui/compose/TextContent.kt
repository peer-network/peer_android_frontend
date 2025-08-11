package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.event.UiModerationEvent

@Composable
fun TextContent(
    post: UiPost,
    uiEngagementEvent: UiEngagementEvent,
    uiModerationEvent: UiModerationEvent,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val updatedConnection by rememberUpdatedState(connection)
    val uiContent = post.mapToContent()
    TextScaffold(
        author = post.author,
        title = post.title,
        caption = post.time,
        description = post.description,
        onAuthorClick = onAuthorClick,
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        engagements = {
            EngagementScreen(
                event = uiEngagementEvent,
                model = uiContent,
                size = 36.dp,
                spacer = 6.dp,
                color = MaterialTheme.colorScheme.onBackground,
                padding = PaddingValues(0.dp),
                orientation = Orientation.Vertical,
            )
        },
        moderation = {
            ModerationScreen(
                uiContent,
                uiModerationEvent,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        header = header,
        actions = {
            updatedConnection(
                Triple(
                    post.author.id,
                    post.author.isfollowing,
                    post.author.isfollowed
                )
            )
        }
    )
}
