package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostStatus(
    time: String,
    modifier: Modifier = Modifier,
    engagement: @Composable () -> Unit
) {
    val updatedEngagement by rememberUpdatedState(engagement)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
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

@Composable
fun PostStatus(
    time: String,
    pinnedBy: String,
    modifier: Modifier = Modifier,
    engagement: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        val pinnedText = buildAnnotatedString {
            append(stringResource(R.string.pin_label))
            append(" ")
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.SemiBold
            )) { append(pinnedBy) }
        }
        Text(
            text = pinnedText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        PostStatus(
            time = time,
            engagement = engagement,
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
    pinnedBy: String? = null,
    onClick: (DesignRichText, String) -> Unit,
    engagement: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val titleWidth = (configuration.screenWidthDp * .3).dp
    val pinnedText = buildAnnotatedString {
        pinnedBy?.let {
            append(stringResource(R.string.pin_label))
            append(" ")
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.SemiBold
            )) { append(pinnedBy) }
        }
    }
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
                .padding(vertical = 10.dp)
        )
        Row {
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.widthIn(max = titleWidth),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                DesignRichText(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    onClick = onClick
                )
                if (description.isNotEmpty()) {
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

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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
    DesignTheme(isDarkMode = false) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            PostStatus(
                time = "2h ago",
                pinnedBy = "Thomas",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) { EngagementReaction(engagement) {} }
            PostStatus(
                time = "2h ago",
                username = "John",
                pinnedBy = "Thomas",
                title = buildAnnotatedString { append("Title") },
                description = buildAnnotatedString { append("Description") },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                onClick = { _,_ -> }
            ) { EngagementReaction(engagement) {} }
        }
    }
}
