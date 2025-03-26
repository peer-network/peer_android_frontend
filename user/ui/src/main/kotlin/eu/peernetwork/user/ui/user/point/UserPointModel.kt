package eu.peernetwork.user.ui.user.point

import eu.peernetwork.core.ui.R

sealed class UserPointModel(
    val icon: Int,
    val label: Int,
) {
    data object Likes : UserPointModel(
        icon = R.drawable.ic_like,
        label = R.string.like_label
    )
    data object Comments : UserPointModel(
        icon = R.drawable.ic_chat,
        label = R.string.comment_label
    )
    data object Posts : UserPointModel(
        icon = R.drawable.ic_add_outline,
        label = R.string.add_label
    )
    companion object {
        val MAP = mapOf<String, UserPointModel>(
            "Likes" to Likes,
            "Comments" to Comments,
            "Posts" to Posts
        )
    }
}
