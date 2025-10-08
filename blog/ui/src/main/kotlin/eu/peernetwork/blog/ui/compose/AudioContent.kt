package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.material.DesignRichText
import eu.peernetwork.core.ui.design.material.DesignTitleStyle

@Composable
fun AudioContent(
    post: UiPost,
    index: Int,
    uiEngagementEvent: UiEngagementEvent,
    uiModerationEvent: UiModerationEvent,
    onAuthorClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    progress: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable () -> Unit = {},
    content: @Composable (UiPost) -> Unit = {}
) {
    val clickHandler by rememberUpdatedState(onAuthorClick)
    val selectHandler by rememberUpdatedState { onPostClick(post.id, index) }
    val updatedBackground by rememberUpdatedState(background)
    val updatedContent by rememberUpdatedState(content)
    val updatedConnection by rememberUpdatedState(connection)
    val uiContent = post.mapToContent()
    MediaScaffold(
        author = post.author,
        onAuthorClick = { clickHandler(post.author.id) },
        description = post.time,
        progress = progress,
        caption = {
            DesignRichText(
                title = uiContent.title,
                description = uiContent.description,
                maxLines = 1,
                maxContentLines = 2,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick,
                style = DesignTitleStyle(
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    ),
                    descriptionStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f)
                    ),
                    span = SpanStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                ),
            )
        },
        engagements = {
            EngagementScreen(
                event = uiEngagementEvent,
                model = uiContent,
                size = 24.dp,
                spacer = 8.dp,
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
        background = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { updatedBackground() }
        },
        actions = {
            updatedConnection(
                Triple(
                    post.author.id,
                    post.author.isfollowing,
                    post.author.isfollowed
                )
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.clickable(
                role = Role.Button,
                onClick = { selectHandler() }
            )) {
                updatedContent(post)
            }
        }
    }
}
