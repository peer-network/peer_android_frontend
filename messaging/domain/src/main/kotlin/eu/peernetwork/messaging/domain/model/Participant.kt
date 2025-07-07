package eu.peernetwork.messaging.domain.model

data class Participant(
    val userId: String,
    val username: String,
    val slug: String,
    val image: String?
)
