package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.extension.cutForExpandable

@Composable
fun DesignTextExpandable(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    maxLinesWhenCollapsed: Int = 2,
    showMoreText: String = stringResource(R.string.view_more_label),
    showLessText: String = stringResource(R.string.view_less_label),
    style: TextStyle,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    content: @Composable (
        display: AnnotatedString,
        maxLines: Int,
        modifier: Modifier,
        style: TextStyle,
        onClick: ((Int) -> Unit)?
    ) -> Unit,
    onAnnotationClick: (String, String) -> Unit = { _, _ -> }
) {

    var cutText by remember { mutableStateOf(text) }
    var hasOverflow by remember { mutableStateOf(false) }
    var ready by remember { mutableStateOf(false) }

    var localExpanded by remember { mutableStateOf(false) }
    val isExpanded = expanded ?: localExpanded
    val changeExpanded = onExpandedChange ?: { localExpanded = it }

    val measurer = rememberTextMeasurer()
    val linkStyle = SpanStyle(
        color = MaterialTheme.colorScheme.tertiary,
        fontWeight = FontWeight.Bold
    )

    val displayText = remember(text, cutText, isExpanded, hasOverflow) {
        buildAnnotatedString {
            if (!isExpanded && hasOverflow) {
                append(cutText)
                append(" ")
                pushStringAnnotation("MORE", "show_more")
                withStyle(linkStyle) { append(showMoreText) }
                pop()
            } else if (isExpanded && hasOverflow) {
                append(text)
                append(" ")
                pushStringAnnotation("LESS", "show_less")
                withStyle(linkStyle) { append(showLessText) }
                pop()
            } else {
                append(text)
            }
        }
    }

    content(
        displayText,
        if (isExpanded) Int.MAX_VALUE else maxLinesWhenCollapsed,
        modifier,
        style
    ) { offset ->
        displayText.getStringAnnotations(offset, offset)
            .firstOrNull()?.let { ann ->
                when (ann.tag) {
                    "MORE", "LESS" -> changeExpanded(!isExpanded)
                    else -> onAnnotationClick(ann.tag, ann.item)
                }
            } ?: run {
            if (hasOverflow) changeExpanded(!isExpanded)
        }
    }

    if (!ready) BasicText(
        text = text,
        style = style,
        maxLines = maxLinesWhenCollapsed,
        overflow = TextOverflow.Clip,
        modifier = Modifier
            .alpha(0f)
            .fillMaxWidth(),
        onTextLayout = { lr ->
            val result = text.cutForExpandable(
                measurer = measurer,
                style = style,
                moreLabel = showMoreText,
                linkStyle = linkStyle,
                maxLines = maxLinesWhenCollapsed,
                maxWidthPx = lr.size.width
            )
            cutText = result.cut
            hasOverflow = result.overflow
            ready = true
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DesignTextExpandableAllScenariosPreview() {
    val style = MaterialTheme.typography.bodyMedium
    val modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)

    val scenarios = listOf(
        "A) Short text (No More)" to "Hello world!",
        "B) Exactly 2 lines (No More)" to "This is line one.\nThis is line two.",
        "C) 3+ lines of text (Has More)" to "This is line one.\nThis is line two.\nThis is line three.\nExtra line four.",
        "D) Very long single word (Has More)" to "SupercalifragilisticexpialidociousEvenMoreWordsWithoutBreaksSupercalifragilisticexpialidociousEvenMoreWordsWithoutBreaks",
        "E) Short emoji (No More)" to "🔥🔥🔥",
        "F) Long emoji (Has More)" to "🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥",
        "G) Single letter line breaks (Has More)" to "A\nB\nC",
        "H) Space before newline (Has More)" to "asdasdsasdasd sdsadasdsa sa\nasdsadsadsadsadsadsa \nsdasdasd",
    )

    androidx.compose.foundation.layout.Column(modifier) {
        scenarios.forEach { (label, contentText) ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            DesignTextExpandable(
                text = buildAnnotatedString { append(contentText) },
                style = style,
                maxLinesWhenCollapsed = 2,
                modifier = modifier,
                content = { displayText, maxLines, mod, style, onClick ->
                    ClickableText(
                        text = displayText,
                        maxLines = maxLines,
                        overflow = TextOverflow.Clip,
                        style = style,
                        modifier = mod,
                        onClick = { offset -> onClick?.invoke(offset) }
                    )
                }
            )
        }
    }
}