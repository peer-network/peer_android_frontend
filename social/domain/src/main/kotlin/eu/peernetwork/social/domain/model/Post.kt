package eu.peernetwork.social.domain.model

data class Post(
    val id: String,
    val title: String,
    val description: String,
    val author: Member
)
