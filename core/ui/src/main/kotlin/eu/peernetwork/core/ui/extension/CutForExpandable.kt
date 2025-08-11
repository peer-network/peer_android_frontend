package eu.peernetwork.core.ui.extension

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.text.TextMeasurer

fun AnnotatedString.cutForExpandable(
    measurer: TextMeasurer,
    style: TextStyle,
    moreLabel: String,
    linkStyle: SpanStyle,
    maxLines: Int,
    maxWidthPx: Int
): CutResult {

    val full = measurer.measure(
        text = this,
        style = style,
        maxLines = Int.MAX_VALUE,
        constraints = Constraints(maxWidth = maxWidthPx)
    )
    if (full.lineCount <= maxLines && !full.hasVisualOverflow) {
        return CutResult(this, overflow = false)
    }

    var left = 0
    var right = length
    var candidate = right

    while (left <= right) {
        val mid = (left + right) / 2
        val slice = sliceWithAnnotations(0, mid)
        val probe = buildProbeText(slice, moreLabel, linkStyle)

        val layout = measurer.measure(
            text = probe,
            style = style,
            maxLines = maxLines,
            constraints = Constraints(maxWidth = maxWidthPx)
        )

        if (layout.lineCount <= maxLines && !layout.hasVisualOverflow) {
            candidate = mid
            left = mid + 1
        } else {
            right = mid - 1
        }
    }

    val cut = if (candidate > 0) sliceWithAnnotations(0, candidate) else this
    return CutResult(cut, overflow = true)
}

private fun buildProbeText(
    cutPart: AnnotatedString,
    moreLabel: String,
    linkStyle: SpanStyle
): AnnotatedString = buildAnnotatedString {
    append(cutPart)
    append(" ")
    append("\u200B")
    withStyle(linkStyle) { append(moreLabel) }
}

@Immutable
data class CutResult(
    val cut: AnnotatedString,
    val overflow: Boolean
)
