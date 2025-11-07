package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.design.luna.DesignAnnotatedText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostFooter(
    time: String,
    engagement: UiPost.Engagement,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        PostAction(engagement)
        Text(
            text = time,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
                .padding(start = 10.dp)
        )
    }
}

@Composable
fun PostFooter(
    time: String,
    pinnedBy: String,
    engagement: UiPost.Engagement,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val pinnedText = buildAnnotatedString {
            append(stringResource(R.string.pin_label))
            append(" ")
            withStyle(style = SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )) { append(pinnedBy) }
        }
        Text(
            text = pinnedText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        PostFooter(
            time = time,
            engagement = engagement,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
fun PostFooter(
    time: String,
    username: String,
    title: AnnotatedString,
    description: AnnotatedString,
    engagement: UiPost.Engagement,
    modifier: Modifier = Modifier,
    pinnedBy: String? = null
) {
    val pinnedText = buildAnnotatedString {
        pinnedBy?.let {
            append(stringResource(R.string.pin_label))
            append(" ")
            withStyle(style = SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )) { append(pinnedBy) }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .then(modifier)
    ) {
        PostFooter(
            time = time,
            engagement = engagement,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 10.dp)
        )
        Row {
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Column(
                modifier = Modifier.weight(1f)
                    .padding(start = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                DesignAnnotatedText(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                if (description.isNotEmpty()) {
                    DesignAnnotatedText(
                        text = description,
                        maxLines = 3,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                        lineHeight = 18.sp
                    )
                }
                pinnedBy?.let {
                    Text(
                        text = pinnedText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewPostFooter() {
    val engagement = UiPost.Engagement(
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
            PostFooter(
                time = "2h ago",
                pinnedBy = "Thomas",
                engagement = engagement,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            PostFooter(
                time = "2h ago",
                username = "John",
                pinnedBy = "Thomas",
                engagement = engagement,
                title = buildAnnotatedString { append("Title") },
                description = buildAnnotatedString { append("Description") },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
