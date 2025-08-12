package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.design.compose.DesignCollapsableText

@Composable
fun PostTitle(
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {}
) {
    val handleMention by rememberUpdatedState(onMentionClick)
    val handleHashTag by rememberUpdatedState(onHashtagClick)
    val uriHandler = LocalUriHandler.current
    Column(modifier = modifier) {
        DesignCollapsableText(
            text = title,
            maxLines = 2,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { offset ->
                val annotations = title.getStringAnnotations(start = offset, end = offset)
                annotations.firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "URL" -> uriHandler.openUri(annotation.item.lowercase())
                        "MENTION" -> handleMention(annotation.item)
                        "HASHTAG" -> handleHashTag(annotation.item)
                    }
                }
            }
        )
        DesignCollapsableText(
            text = description,
            maxLines = 3,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.tertiary,
                lineHeight = 18.sp
            ),
            modifier = Modifier.padding(top = 4.dp),
            onClick = { offset ->
                val annotations = description.getStringAnnotations(start = offset, end = offset)
                annotations.firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "URL" -> uriHandler.openUri(annotation.item.lowercase())
                        "MENTION" -> handleMention(annotation.item)
                        "HASHTAG" -> handleHashTag(annotation.item)
                    }
                }
            }
        )
    }
}
