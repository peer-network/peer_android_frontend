package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.core.ui.design.material.DesignRichText
import eu.peernetwork.core.ui.design.material.DesignTitleStyle

@Composable
fun PhotoContent(
    post: UiPost,
    uiEngagementEvent: UiEngagementEvent,
    uiModerationEvent: UiModerationEvent,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    indicator: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable () -> Unit = {},
    content: @Composable (UiPost) -> Unit = {}
) {
    val clickHandler by rememberUpdatedState(onAuthorClick)
    val updatedContent by rememberUpdatedState(content)
    val updatedConnection by rememberUpdatedState(connection)
    val uiContent = post.mapToContent()
    PhotoScaffold(
        author = post.author,
        onAuthorClick = { clickHandler(post.author.id) },
        description = post.time,
        indicator = indicator,
        caption = {
            Column(modifier = Modifier.heightIn(max = 128.dp)
                .verticalScroll(rememberScrollState())) {
                DesignRichText(
                    uiContent.title,
                    uiContent.description,
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
            }
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
        background = background,
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
        ) { updatedContent(post) }
    }
}
