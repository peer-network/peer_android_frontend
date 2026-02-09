package eu.peernetwork.blog.ui.model

data class UiReaction(
    val isLiked: Boolean?,
    val isDisliked: Boolean?,
    val isViewed: Boolean?,
    val commented: Int?
)
