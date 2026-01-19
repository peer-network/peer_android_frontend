package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.extension.tap
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostStatus(
    time: String,
    modifier: Modifier = Modifier,
    reported: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    engagement: @Composable () -> Unit
) {
    val updatedEngagement by rememberUpdatedState(engagement)
    val updatedLabel by rememberUpdatedState(label)
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)
                .padding(bottom = 2.dp)) {
                updatedLabel?.invoke()
            }
            if (reported) {
                PostReportLabel()
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            updatedEngagement()
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun PostStatus(
    time: String,
    modifier: Modifier = Modifier,
    reported: Boolean = false,
    isAuthor: Boolean = false,
    isAccessible: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        PostStatus(
            time = time,
            engagement = content,
            reported = reported && !(!isAccessible && isAuthor),
            label = if (!isAccessible && isAuthor) {
                { PostVisibilityLabel() }
            } else {
                null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
fun PostStatus(
    time: String,
    username: String,
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    isAuthor: Boolean = false,
    isAccessible: Boolean = false,
    reported: Boolean = false,
    onAuthorClick: () -> Unit,
    onClick: (DesignRichText, String) -> Unit,
    engagement: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val titleWidth = (configuration.screenWidthDp * .3).dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        PostStatus(
            time = time,
            engagement = engagement,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Row {
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.widthIn(max = titleWidth)
                    .tap(onAuthorClick),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PostTextMask(
                        isVisible = isVisible,
                        modifier = Modifier.weight(1f)
                    ) {
                        DesignRichText(
                            text = title,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            onClick = onClick,
                        )
                    }
                    if (!isAccessible && isAuthor) {
                        PostVisibilityLabel()
                    } else if (reported) {
                        PostReportLabel()
                    }
                }
                if (description.isNotEmpty()) {
                    PostTextMask(
                        isVisible = isVisible,
                        fraction = .6f
                    ) {
                        DesignRichText(
                            text = description,
                            maxLines = 3,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            lineHeight = 18.sp,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPostStatus() {
    val engagement = UiEngagement(
        id = "<test-id>",
        likes = "5k",
        dislikes = "1k",
        isDisliked = false,
        isLiked = false,
        views = "3k",
        comment = "1k"
    )
    DesignTheme(isDarkMode = true) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            PostStatus(
                time = "2h ago",
                reported = true,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                content = { EngagementReaction(engagement) {} }
            )
            PostStatus(
                time = "2h ago",
                username = "John",
                reported = true,
                isAuthor = true,
                title = buildAnnotatedString { append("Title") },
                description = buildAnnotatedString { append("Description") },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                onAuthorClick = {},
                onClick = { _,_ -> }
            ) { EngagementReaction(engagement) {} }
        }
    }
}
