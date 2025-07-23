package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxContentLines: Int = 3,
    minimizedMaxLines: Int = 3,
    showMoreText: String = "... More",
    showLessText: String = "Less",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    linkColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.tertiary,
    linkFontWeight: FontWeight = FontWeight.Bold,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var cutIndex by remember { mutableIntStateOf(text.length) }
    var showLink by remember { mutableStateOf(false) }
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var ready by remember { mutableStateOf(false) }

    SubcomposeLayout(modifier = modifier.animateContentSize()) { constraints ->

        layoutResult = null
        subcompose("measure") {
            Text(
                text = text,
                style = textStyle,
                maxLines = minimizedMaxLines,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (!ready) {
                        layoutResult = result
                        if (result.hasVisualOverflow) {
                            val lastLine = minimizedMaxLines - 1
                            val end = (result.getLineEnd(lastLine, visibleEnd = true) - showMoreText.length)
                                .coerceAtLeast(0)
                            cutIndex = end
                            showLink = true
                        } else {
                            cutIndex = text.length
                            showLink = false
                        }
                        ready = true
                    }
                }
            )
        }.first().measure(constraints)

        val linkNeeded = showLink || isExpanded
        val linkText = if (isExpanded) showLessText else showMoreText
        val linkPlaceable = if (linkNeeded) {
            subcompose("link") {
                Text(
                    text = linkText,
                    style = textStyle.copy(fontWeight = linkFontWeight),
                    color = linkColor,
                    modifier = Modifier
                        .clickable {
                            isExpanded = !isExpanded
                            ready = false
                        }
                        .padding(start = 2.dp)
                )
            }.first().measure(constraints)
        } else null

        val lastLine = (minimizedMaxLines - 1).coerceAtLeast(0)
        var finalCut = if (isExpanded) text.length else cutIndex
        var inlinePos: Pair<Int,Int>? = null
        if (!isExpanded && showLink && linkPlaceable != null) {

            while (true) {
                var tmpLayout: TextLayoutResult? = null
                val tmpPlaceable = subcompose("mainCheck")
                {
                    Text(
                        text = text.take(finalCut),
                        style = textStyle,
                        maxLines = minimizedMaxLines,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { result ->
                            tmpLayout = result
                        }
                    )
                }.first().measure(constraints)

                val lr = tmpLayout
                if (lr != null) {
                    val lineRight = lr.getLineRight(lastLine).roundToInt()
                    if (lineRight + linkPlaceable.width <= constraints.maxWidth) {
                        val lineTop = lr.getLineTop(lastLine).roundToInt()
                        inlinePos = lineRight to lineTop
                        break
                    }
                }
                if (finalCut <= 0) break
                finalCut--
            }
        }


        val displayText = if (isExpanded) text else text.take(finalCut)
        val mainPlaceable = subcompose("main") {
            Text(
                text = displayText,
                style = textStyle,
                maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }.first().measure(constraints)


        val width = constraints.maxWidth
        val height = mainPlaceable.height

        layout(width, height) {
            mainPlaceable.place(0, 0)
            linkPlaceable?.let { lp ->
                if (!isExpanded && inlinePos != null) {
                    lp.place(inlinePos.first, inlinePos.second)
                } else {

                    val x = (mainPlaceable.width - lp.width).coerceAtLeast(0)
                    val y = (mainPlaceable.height - lp.height).coerceAtLeast(0)
                    lp.place(x, y)
                }
            }
        }
    }
}
