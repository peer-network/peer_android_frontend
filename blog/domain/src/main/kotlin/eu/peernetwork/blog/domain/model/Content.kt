package eu.peernetwork.blog.domain.model

data class Content(
    val id: String,
    val title: String,
    val type: ContentType
)
