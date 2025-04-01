package eu.peernetwork.blog.domain.model

data class Comment(
    val id: String,
    val content: String,
    val author: Author
)
