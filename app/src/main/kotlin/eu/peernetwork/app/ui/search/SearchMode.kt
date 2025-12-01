package eu.peernetwork.app.ui.search

sealed interface SearchMode {
    data object Default : SearchMode
    data object Username : SearchMode
    data object Tag: SearchMode
    data object Title: SearchMode
}
