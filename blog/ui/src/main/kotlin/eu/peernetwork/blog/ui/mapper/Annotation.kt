package eu.peernetwork.blog.ui.mapper

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

fun CharSequence.annotateTag(style: SpanStyle): AnnotatedString {
    return buildAnnotatedString {
        append(this@annotateTag)
        val tagRegex = Regex("#\\w+")
        tagRegex.findAll(this@annotateTag).forEach { match ->
            addStyle(
                style = style,
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }
}
