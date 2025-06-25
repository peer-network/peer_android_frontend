package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun PostText(
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
        ClickableText(
            text = title,
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
        ClickableText(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary,
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
