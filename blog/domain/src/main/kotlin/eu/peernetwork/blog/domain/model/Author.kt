package eu.peernetwork.blog.domain.model

data class Author(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val isfollowing: Boolean,
    val isfollowed:Boolean
)
