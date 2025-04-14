package eu.peernetwork.blog.ui.point

import eu.peernetwork.core.ui.R

sealed class BlogPointModel(
    val icon: Int,
    val label: Int,
) {
    data object Likes : BlogPointModel(
        icon = R.drawable.ic_like,
        label = R.string.like_label
    )
    data object Comments : BlogPointModel(
        icon = R.drawable.ic_chat,
        label = R.string.comment_label
    )
    data object Posts : BlogPointModel(
        icon = R.drawable.ic_add_outline,
        label = R.string.add_label
    )
    companion object {
        val MAP = mapOf<String, BlogPointModel>(
            "Likes" to Likes,
            "Comments" to Comments,
            "Posts" to Posts
        )
    }
}
