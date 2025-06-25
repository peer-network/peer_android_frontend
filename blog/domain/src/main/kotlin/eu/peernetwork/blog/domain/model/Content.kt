package eu.peernetwork.blog.domain.model

data class Content(
    val id: String,
    val title: String,
    val description: String,
    val media: List<Media>,
    val author: Author,
    val type: Type,
    val createdAt: Long,
    val likes: Int,
    val dislikes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val comment: Int
) {
    enum class Type {
        IMAGE,
        VIDEO,
        AUDIO,
        TEXT,
        FOLLOWED,
        FOLLOWER
    }
}
