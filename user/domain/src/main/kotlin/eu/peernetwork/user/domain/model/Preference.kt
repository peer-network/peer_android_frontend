package eu.peernetwork.user.domain.model

data class Preference(
    val mode: Mode,
    val flags: List<String>
)
