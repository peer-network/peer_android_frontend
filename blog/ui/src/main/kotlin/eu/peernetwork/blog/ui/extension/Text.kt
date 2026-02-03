package eu.peernetwork.blog.ui.extension

fun String.normalizeWhitespaces(): String {
    return replace(Regex("\\s{2,}"), " ").trim()
}
