package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R

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

    var isExpanded by remember { mutableStateOf(false) }
    var readyToDraw by remember { mutableStateOf(false) }
    var cutDescription by remember { mutableStateOf(description) }
    var showMoreNeeded by remember { mutableStateOf(false) }


    val showMoreText = stringResource(id = R.string.show_more_text)
    val showLessText = stringResource(id = R.string.show_less_text)

    val linkColor = MaterialTheme.colorScheme.tertiary
    val linkFontWeight = FontWeight.Bold

    fun AnnotatedString.sliceAnnotatedString(start: Int, end: Int): AnnotatedString {
        if (start >= end || start < 0 || end > this.length) return AnnotatedString("")

        val subText = this.text.substring(start, end)
        val builder = AnnotatedString.Builder(subText)

        this.spanStyles.filter { style ->
            style.start < end && style.end > start
        }.forEach { style ->
            val sliceStart = (style.start - start).coerceAtLeast(0)
            val sliceEnd = (style.end - start).coerceAtMost(subText.length)
            if (sliceStart < sliceEnd) {
                builder.addStyle(style.item, sliceStart, sliceEnd)
            }
        }

        this.getStringAnnotations(start, end).forEach { annotation ->
            val sliceStart = (annotation.start - start).coerceAtLeast(0)
            val sliceEnd = (annotation.end - start).coerceAtMost(subText.length)
            if (sliceStart < sliceEnd) {
                builder.addStringAnnotation(annotation.tag, annotation.item, sliceStart, sliceEnd)
            }
        }

        return builder.toAnnotatedString()
    }

    val annotatedDescription = remember(isExpanded, cutDescription, showMoreNeeded) {
        if (isExpanded) {
            buildAnnotatedString {
                append(cutDescription)
                append(" ")
                pushStringAnnotation(tag = "SHOW_LESS", annotation = "show_less")
                withStyle(
                    style = SpanStyle(color = linkColor, fontWeight = linkFontWeight)
                ) {
                    append(showLessText)
                }
                pop()
            }
        } else {
            buildAnnotatedString {
                append(cutDescription)
                if (showMoreNeeded) {
                    pushStringAnnotation(tag = "SHOW_MORE", annotation = "show_more")
                    withStyle(
                        style = SpanStyle(color = linkColor, fontWeight = linkFontWeight)
                    ) {
                        append(showMoreText)
                    }
                    pop()
                }
            }
        }
    }

    Column(modifier = modifier) {
        ClickableText(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
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
            text = annotatedDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary,
            ),
            modifier = Modifier.padding(top = 4.dp),
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->

                if (!isExpanded) {
                    if (!readyToDraw) {
                        if (layoutResult.hasVisualOverflow) {
                            val lastVisibleCharIndex = layoutResult.getLineEnd(2, visibleEnd = true)
                            val cutoffIndex = (lastVisibleCharIndex - showMoreText.length).coerceAtLeast(0)
                            cutDescription = description.sliceAnnotatedString(0, cutoffIndex)
                            showMoreNeeded = true
                        } else {
                            cutDescription = description
                            showMoreNeeded = false
                        }
                        readyToDraw = true
                    }
                } else {

                    cutDescription = description
                    showMoreNeeded = false
                    readyToDraw = false
                }
            },
            onClick = { offset ->
                annotatedDescription.getStringAnnotations(tag = "SHOW_MORE", start = offset, end = offset)
                    .firstOrNull()?.let {
                        isExpanded = true
                        readyToDraw = false
                        return@ClickableText
                    }
                annotatedDescription.getStringAnnotations(tag = "SHOW_LESS", start = offset, end = offset)
                    .firstOrNull()?.let {
                        isExpanded = false
                        readyToDraw = false
                        return@ClickableText
                    }

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
