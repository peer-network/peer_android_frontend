package eu.peernetwork.user.domain.model

sealed class Mode(val value: String) {
    data class Safe(private val mode: String) : Mode(mode)
    data class Sensitive(private val mode: String) : Mode(mode)
}
