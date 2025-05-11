package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DesignRichTitle(
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    maxContentLines: Int = Int.MAX_VALUE,
    spacer: @Composable () -> Unit = {},
    style: DesignTitleTextStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    titleOnClick: (() -> Unit)? = null,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {}
) {
    val textStyle = style ?: DesignTitleTextStyle(
        span = SpanStyle(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.tertiary
        ),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        ),
        descriptionStyle = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.tertiary
        )
    )
    val uriHandler = LocalUriHandler.current
    val handleTitleOnClick by rememberUpdatedState(titleOnClick)
    val handleMentionClick by rememberUpdatedState(onMentionClick)
    val handleHashtagClick by rememberUpdatedState(onHashtagClick)
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        ClickableText(
            text = title,
            style = textStyle.style,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            onClick = { offset ->
                val annotations = title.getStringAnnotations(start = offset, end = offset)
                annotations.firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "URL" -> uriHandler.openUri(annotation.item.lowercase())
                        "MENTION" -> handleMentionClick(annotation.item)
                        "HASHTAG" -> handleHashtagClick(annotation.item)
                    }
                } ?: handleTitleOnClick?.invoke()
            }
        )
        spacer()
        if (description.isNotEmpty()) {
            ClickableText(
                text = description,
                maxLines = maxContentLines,
                overflow = TextOverflow.Ellipsis,
                style = textStyle.descriptionStyle,
                modifier = Modifier.padding(top = 2.dp),
                onClick = { offset ->
                    val annotations = description.getStringAnnotations(start = offset, end = offset)
                    annotations.firstOrNull()?.let { annotation ->
                        when (annotation.tag) {
                            "URL" -> uriHandler.openUri(annotation.item.lowercase())
                            "MENTION" -> handleMentionClick(annotation.item)
                            "HASHTAG" -> handleHashtagClick(annotation.item)
                        }
                    }
                }
            )
        }
    }
}
