package eu.peernetwork.social.domain.model

data class Block(
    val userId: String,
    val username: String,
    val slug: Int,
    val image: String
)
