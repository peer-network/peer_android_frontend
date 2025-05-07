package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.mapper.annotateTag
import eu.peernetwork.core.ui.design.compose.DesignTitleTextStyle

@Composable
fun PostText(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    style: DesignTitleTextStyle? = null,
    mentionColor: Color = MaterialTheme.colorScheme.primary,
    hashtagColor: Color = MaterialTheme.colorScheme.primary,
    linkColor: Color = MaterialTheme.colorScheme.primary,
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
                    append(description.substring(lastIndex))
                }
            }
        }
    }
    Column(modifier = modifier) {
        ClickableText(
            text = annotatedTitle,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { offset ->
                annotatedTitle.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item.lowercase())
                    }
            }
        )
        ClickableText(
            text = annotatedDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary,
            ),
            modifier = Modifier.padding(top = 4.dp),
            onClick = { offset ->
                annotatedDescription.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item.lowercase())
                    }
            }
        )
    }
}
