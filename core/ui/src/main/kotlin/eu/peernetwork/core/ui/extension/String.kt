package eu.peernetwork.core.ui.extension

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun String.annotate(): AnnotatedString {
    return buildAnnotatedString { append(this@annotate) }
}

fun String.annotate(text: String, style: SpanStyle): AnnotatedString {
    return buildAnnotatedString {
        val regex = Regex.fromLiteral(text)
        var lastIndex = 0
        regex.findAll(this@annotate).forEach { matchResult ->
            append(this@annotate.substring(lastIndex, matchResult.range.first))
            withStyle(style) {
                append(matchResult.value)
            }
            lastIndex = matchResult.range.last + 1
        }
        if (lastIndex < this@annotate.length) {
            append(this@annotate.substring(lastIndex))
        }
    }
}
