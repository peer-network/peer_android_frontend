package eu.peernetwork.user.domain.model

data class Token(
    val access: String,
    val refresh: String,
    val expiresIn: Long,
)
