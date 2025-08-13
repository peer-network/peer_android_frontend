package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R

@Composable
fun DesignTextExpandable(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    maxLinesWhenCollapsed: Int = 2,
    style: TextStyle = LocalTextStyle.current,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onAnnotationClick: (String, String) -> Unit = { _, _ -> }
) {
    var localExpanded by remember { mutableStateOf(false) }
    val isExpanded = expanded ?: localExpanded
    val changeExpanded = onExpandedChange ?: { localExpanded = it }

    val linkStyle = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)

    Layout(
        modifier = modifier.animateContentSize(),
        content = {
            ClickableText(
                text = text,
                maxLines = maxLinesWhenCollapsed,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { changeExpanded(!isExpanded) },
                style = style,
                onClick = { offset ->
                    text.getStringAnnotations(start = offset, end = offset)
                        .firstOrNull()?.let { ann ->
                            onAnnotationClick(ann.tag, ann.item)
                        }
                }
            )
            ClickableText(
                text = text,
                maxLines = Int.MAX_VALUE,
                style = style,
                modifier = Modifier.clickable { changeExpanded(!isExpanded) },
                onClick = { offset ->
                    text.getStringAnnotations(start = offset, end = offset)
                        .firstOrNull()?.let { ann ->
                            onAnnotationClick(ann.tag, ann.item)
                        }
                }
            )
            Text(
                text = if (isExpanded) {
                    stringResource(R.string.view_less_label)
                } else {
                    stringResource(R.string.view_more_label)
                },
                modifier = Modifier.clickable { changeExpanded(!isExpanded) },
                style = style.copy(fontWeight = FontWeight.SemiBold, color = linkStyle.color)
            )
        }
    ) { measurables, constraints ->
        val collapsedPlaceable = measurables[0].measure(constraints)
        val fullPlaceable = measurables[1].measure(constraints.copy(maxHeight = Int.MAX_VALUE))
        val labelPlaceable = measurables[2].measure(constraints)

        val needsMore = fullPlaceable.height > collapsedPlaceable.height
        val width = constraints.maxWidth
        val height = if (isExpanded) {
            fullPlaceable.height + if (needsMore) labelPlaceable.height else 0
        } else {
            collapsedPlaceable.height + if (needsMore) labelPlaceable.height else 0
        }

        layout(width, height) {
            if (isExpanded) {
                fullPlaceable.place(0, 0)
                if (needsMore) {
                    labelPlaceable.place(0, fullPlaceable.height)
                }
            } else {
                collapsedPlaceable.place(0, 0)
                if (needsMore) {
                    labelPlaceable.place(0, collapsedPlaceable.height)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DesignTextExpandableAllScenariosPreview() {
    val style = MaterialTheme.typography.bodyMedium
    val outer = Modifier
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
        "H) Space before newline (Has More)" to "asdasdsasdasd sdsadasdsa sa\nasdsadsadsadsadsadsa \nsdasdasd"
    )

    Column(outer) {
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}