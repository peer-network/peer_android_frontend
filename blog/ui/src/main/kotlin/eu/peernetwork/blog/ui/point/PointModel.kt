package eu.peernetwork.blog.ui.point

import eu.peernetwork.core.ui.R

sealed class PointModel(
    val icon: Int,
    val label: Int,
) {
    data object Likes : PointModel(
        icon = R.drawable.ic_like,
        label = R.string.like_label
    )
    data object Comments : PointModel(
        icon = R.drawable.ic_chat,
        label = R.string.comment_label
    )
    data object Posts : PointModel(
        icon = R.drawable.ic_add_outline,
        label = R.string.add_label
    )
    companion object {
        val MAP = mapOf<String, PointModel>(
            "Likes" to Likes,
            "Comments" to Comments,
            "Posts" to Posts
        )
    }
}
