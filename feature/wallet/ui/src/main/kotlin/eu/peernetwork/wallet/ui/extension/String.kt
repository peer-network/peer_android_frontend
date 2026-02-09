package eu.peernetwork.wallet.ui.extension

fun String.normalizeWhitespaces(): String {
    return replace(Regex("\\s{2,}"), " ").trim()
}
