package eu.peernetwork.core.ui.extension

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun String.annotate(selector: Map<String, SpanStyle>): AnnotatedString {
    return buildAnnotatedString {
        val regex = """\w+""".toRegex()
        var lastIndex = 0
        regex.findAll(this@annotate).forEach { matchResult ->
            val word = matchResult.value
            val start = matchResult.range.first
            append(this@annotate.substring(lastIndex, start))
            withStyle(selector[word] ?: SpanStyle()) {
                append(word)
            }
            lastIndex = matchResult.range.last + 1
        }
        if (lastIndex < this@annotate.length) {
            append(this@annotate.substring(lastIndex))
        }
    }
}