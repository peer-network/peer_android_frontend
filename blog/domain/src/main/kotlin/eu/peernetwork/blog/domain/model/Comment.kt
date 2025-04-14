package eu.peernetwork.blog.domain.model

data class Comment(
    val id: String,
    val content: String,
    val author: Author,
    val createdAt: Long,
    val likes: Int
)
