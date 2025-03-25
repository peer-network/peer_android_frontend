package eu.peernetwork.user.ui.user.coupon

import eu.peernetwork.core.ui.R

sealed class UserCouponModel(
    val icon: Int,
    val label: Int,
) {
    data object Likes : UserCouponModel(
        icon = R.drawable.ic_like,
        label = R.string.like_label
    )
    data object Comments : UserCouponModel(
        icon = R.drawable.ic_chat,
        label = R.string.comment_label
    )
    data object Posts : UserCouponModel(
        icon = R.drawable.ic_add_outline,
        label = R.string.add_label
    )
    companion object {
        val MAP = mapOf<String, UserCouponModel>(
            "Likes" to Likes,
            "Comments" to Comments,
            "Posts" to Posts
        )
    }
}
