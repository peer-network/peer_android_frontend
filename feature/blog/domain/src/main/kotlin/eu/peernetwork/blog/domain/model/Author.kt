package eu.peernetwork.blog.domain.model

data class Author(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val following: Boolean,
    val followed: Boolean,
    val isAccessible: Boolean,
    val status: Status,
)
