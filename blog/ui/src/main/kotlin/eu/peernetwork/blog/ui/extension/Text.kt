package eu.peernetwork.blog.ui.extension

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString.normalizeWhitespaces(): String {
    return replace(Regex("\\s{2,}"), " ").trim()
}
