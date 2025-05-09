package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun DesignRichTitle(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    maxContentLines: Int = Int.MAX_VALUE,
    spacer: @Composable () -> Unit = {},
    style: DesignTitleTextStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    mentionColor: Color = MaterialTheme.colorScheme.primary,
    hashtagColor: Color = MaterialTheme.colorScheme.primary,
    linkColor: Color = MaterialTheme.colorScheme.primary,
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
    val annotatedDescription by remember(description) {
        derivedStateOf {
            val pattern = Regex("""(@\w+)|(#\w+)|((https?|ftp)://[^\s]+)""", RegexOption.IGNORE_CASE)
            buildAnnotatedString {
                val matches = pattern.findAll(description)
                var lastIndex = 0
                matches.forEach { match ->
                    val value = match.value
                    append(description.substring(lastIndex, match.range.first))
                    val annotationTag = when {
                        value.startsWith("http", true) || value.startsWith("ftp", true) -> "URL"
                        value.startsWith("@") -> "MENTION"
                        value.startsWith("#") -> "HASHTAG"
                        else -> "PLAIN"
                    }
                    val styleColor = when (annotationTag) {
                        "URL" -> linkColor
                        "MENTION" -> mentionColor
                        "HASHTAG" -> hashtagColor
                        else -> textStyle.descriptionStyle.color
                    }

                    pushStringAnnotation(tag = annotationTag, annotation = value)
                    withStyle(style = SpanStyle(color = styleColor)) {
                        append(value)
                    }
                    pop()
                    lastIndex = match.range.last + 1
                }
                if (lastIndex < description.length) {
                    append(description.substring(lastIndex))
                }
            }
        }
    }
    val annotatedTitle by remember(title) {
        derivedStateOf {
            val pattern = Regex("""(@\w+)|((https?|ftp)://[^\s]+)""", RegexOption.IGNORE_CASE)
            buildAnnotatedString {
                val matches = pattern.findAll(title)
                var lastIndex = 0
                matches.forEach { match ->
                    val value = match.value
                    append(title.substring(lastIndex, match.range.first))
                    val annotationTag = when {
                        value.startsWith("http", true) || value.startsWith("ftp", true) -> "URL"
                        value.startsWith("@") -> "MENTION"
                        else -> "PLAIN"
                    }
                    val styleColor = when (annotationTag) {
                        "URL" -> linkColor
                        "MENTION" -> mentionColor
                        else -> textStyle.style.color
                    }

                    pushStringAnnotation(tag = annotationTag, annotation = value)
                    withStyle(style = SpanStyle(color = styleColor)) {
                        append(value)
                    }
                    pop()
                    lastIndex = match.range.last + 1
                }
                if (lastIndex < title.length) {
                    append(title.substring(lastIndex))
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        ClickableText(
            text = annotatedTitle,
            style = textStyle.style,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            modifier = Modifier.then(
                if (titleOnClick != null) Modifier.clickable { titleOnClick() } else Modifier
            ),
            onClick = { offset ->
                val annotations = annotatedTitle.getStringAnnotations(start = offset, end = offset)
                annotations.firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "URL" -> uriHandler.openUri(annotation.item.lowercase())
                        "MENTION" -> onMentionClick(annotation.item)
                        "HASHTAG" -> onHashtagClick(annotation.item)
                    }
                }
            }
        )
        spacer()
        if (description.isNotEmpty()) {
            ClickableText(
                text = annotatedDescription,
                maxLines = maxContentLines,
                overflow = TextOverflow.Ellipsis,
                style = textStyle.descriptionStyle,
                modifier = Modifier.padding(top = 2.dp),
                onClick = { offset ->
                    val annotations = annotatedDescription.getStringAnnotations(start = offset, end = offset)
                    annotations.firstOrNull()?.let { annotation ->
                        when (annotation.tag) {
                            "URL" -> uriHandler.openUri(annotation.item.lowercase())
                            "MENTION" -> onMentionClick(annotation.item)
                            "HASHTAG" -> onHashtagClick(annotation.item)
                        }
                    }
                }
            )
        }
    }
}
