package eu.peernetwork.blog.ui.model

import androidx.annotation.DrawableRes
import eu.peernetwork.blog.ui.R

sealed class UiAction(
    @DrawableRes val id: Int,
    val label: Int? = null,
) {
    data object Like: UiAction(
        R.drawable.ic_like,
        R.string.like_label,
    )
    data object Dislike: UiAction(
        R.drawable.ic_dislike,
        R.string.dislike_label,
    )
    data object Comment: UiAction(
        R.drawable.ic_chat,
        R.string.comment_label,
    )
    data object View: UiAction(
        R.drawable.ic_view,
        R.string.view_label,
    )
    companion object {
        val ENGAGEMENTS = arrayOf(Like, Dislike, Comment, View)
    }
}
