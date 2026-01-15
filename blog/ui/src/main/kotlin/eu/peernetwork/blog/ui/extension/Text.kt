package eu.peernetwork.blog.ui.extension

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString.normalizeWhitespaces(): AnnotatedString {
    val sanitizedText = this.text.replace(Regex("\\s{2,}"), " ").trim()
    return AnnotatedString(sanitizedText, this.spanStyles, this.paragraphStyles)
}
