package eu.peernetwork.wallet.ui.reward

import eu.peernetwork.core.ui.R

sealed class RewardType(
    val icon: Int,
    val label: Int,
) {
    data object Likes : RewardType(
        icon = R.drawable.ic_like,
        label = R.string.like_label
    )
    data object Comments : RewardType(
        icon = R.drawable.ic_chat,
        label = R.string.comment_label
    )
    data object Posts : RewardType(
        icon = R.drawable.ic_add_outline,
        label = R.string.add_label
    )
    companion object {
        val MAP = mapOf<String, RewardType>(
            "Likes" to Likes,
            "Comments" to Comments,
            "Posts" to Posts
        )
    }
}
