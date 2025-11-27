package eu.peernetwork.blog.ui.model.v2

data class UiEngagement(
    val id: String,
    val likes: String,
    val dislikes: String,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val views: String,
    val comment: String
)
