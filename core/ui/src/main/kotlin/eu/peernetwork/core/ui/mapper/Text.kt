package eu.peernetwork.core.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import eu.peernetwork.core.ui.theme.PrimaryColor

fun String.annotate(color: Color = PrimaryColor): AnnotatedString {
    val combinedPattern = Regex("""(?<!\w)@\w+|https?://[^\s]+|www\.[^\s]+|(?<![@\w])(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}(?:/[^\s]*)?|(?<!\w)#\w+""")
    val matches = combinedPattern.findAll(this)
    return buildAnnotatedString {
        var currentIndex = 0
        for (match in matches) {
            val tag = if (match.value[0] == '@' || match.value[0] == '#') {
                match.value[0].toString()
            } else "*"
            append(substring(currentIndex, match.range.first))
            pushStringAnnotation(tag = tag, annotation = tag)
            withStyle(style = SpanStyle(color = color)) {
                append(match.value)
            }
            pop()
            currentIndex = match.range.last + 1
        }
        if (currentIndex < this@annotate.length) {
            append(substring(currentIndex))
        }
    }
}
