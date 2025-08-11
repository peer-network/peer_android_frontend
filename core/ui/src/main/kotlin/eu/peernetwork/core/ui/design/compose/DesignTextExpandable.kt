package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.extension.CutResult
import eu.peernetwork.core.ui.extension.cutForExpandable

data class CutKey(
    val widthPx: Int,
    val textHash: Int,
    val maxLines: Int,
    val moreLabel: String
)

data class CutCache(
    val key: CutKey,
    val cut: AnnotatedString,
    val overflow: Boolean
)

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
    ) -> Unit = { display, max, mod, stl, onClick ->
        ClickableText(
            text = display,
            maxLines = max,
            style = stl,
            modifier = mod,
            onClick = { off -> onClick?.invoke(off) }
        )
    },
    onAnnotationClick: (String, String) -> Unit = { _, _ -> }
) {
    val measurer = rememberTextMeasurer()
    var localExpanded by rememberSaveable(text.text) { mutableStateOf(false) }
    val isExpanded = expanded ?: localExpanded
    val changeExpanded = onExpandedChange ?: { localExpanded = it }
    val linkStyle = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
    var cutCache by remember { mutableStateOf<CutCache?>(null) }
    val stableTextHash = remember(text) { text.text.hashCode() }

    SubcomposeLayout(modifier = modifier.animateContentSize()) { constraints ->
        val widthPx = constraints.maxWidth
        val key = CutKey(widthPx = widthPx, textHash = stableTextHash, maxLines = maxLinesWhenCollapsed, moreLabel = showMoreText)
        val cutResult = if (cutCache?.key == key) {
            CutResult(cut = cutCache!!.cut, overflow = cutCache!!.overflow)
        } else {
            val computed = text.cutForExpandable(
                measurer = measurer,
                style = style,
                moreLabel = showMoreText,
                linkStyle = linkStyle,
                maxLines = maxLinesWhenCollapsed,
                maxWidthPx = widthPx
            )
            cutCache = CutCache(key = key, cut = computed.cut, overflow = computed.overflow)
            computed
        }

        val displayText = buildAnnotatedString {
            if (!isExpanded && cutResult.overflow) {
                append(cutResult.cut)
                append(" ")
                pushStringAnnotation("MORE", "show_more")
                withStyle(linkStyle) { append(showMoreText) }
                pop()
            } else if (isExpanded && cutResult.overflow) {
                append(text)
                append(" ")
                pushStringAnnotation("LESS", "show_less")
                withStyle(linkStyle) { append(showLessText) }
                pop()
            } else {
                append(text)
            }
        }

        val placeable = subcompose("main") {
            content(
                displayText,
                if (isExpanded) Int.MAX_VALUE else maxLinesWhenCollapsed,
                Modifier,
                style
            ) { offset ->
                displayText.getStringAnnotations(offset, offset).firstOrNull()?.let { ann ->
                    when (ann.tag) {
                        "MORE", "LESS" -> changeExpanded(!isExpanded)
                        else -> onAnnotationClick(ann.tag, ann.item)
                    }
                } ?: run {
                    if (cutResult.overflow) changeExpanded(!isExpanded)
                }
            }
        }.first().measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DesignTextExpandableAllScenariosPreview() {
    val style = MaterialTheme.typography.bodyMedium
    val outer = Modifier.fillMaxWidth().padding(12.dp)
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
        }
    }
}