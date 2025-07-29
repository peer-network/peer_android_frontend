package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.Engagements
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.Moderations
import eu.peernetwork.core.ui.design.compose.DesignRichText
import eu.peernetwork.core.ui.design.compose.DesignTitleStyle

@Composable
fun PhotoContent(
    id: String,
    post: UiPost,
    engagements: Engagements,
    moderations: Moderations,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    indicator: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable () -> Unit = {},
    content: @Composable (UiPost) -> Unit = {}
) {
    var descriptionExpanded by remember { mutableStateOf(false) }
    val clickHandler by rememberUpdatedState(onAuthorClick)
    val updatedContent by rememberUpdatedState(content)
    val updatedConnection by rememberUpdatedState(connection)
    val uiContent = post.mapToContent()

    PhotoScaffold(
        author = post.author,
        onAuthorClick = { clickHandler(post.author.id) },
        description = post.time,
        indicator = indicator,
        caption = { expanded, onExpandedChange ->
            DesignRichText(
                uiContent.title,
                uiContent.description,
                maxLines = 1,
                maxContentLines = 2,
                expanded = expanded,
                onExpandedChange = onExpandedChange,
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
        descriptionExpanded = descriptionExpanded,
        onDescriptionExpandedChange = { descriptionExpanded = it },
        engagements = {
            EngagementScreen(
                event = engagements,
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
                moderations,
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
        },
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { updatedContent(post) }
        })
}
