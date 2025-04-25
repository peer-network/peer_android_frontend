package eu.peernetwork.blog.ui.model

data class UiEngagement(
    val id: String,
    val likes: Int,
    val dislikes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val comment: Int
)
