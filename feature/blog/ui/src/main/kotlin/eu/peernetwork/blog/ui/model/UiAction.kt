package eu.peernetwork.blog.ui.model

import androidx.annotation.DrawableRes
import eu.peernetwork.feature.blog.ui.R

sealed class UiAction(
    @DrawableRes val id: Int,
    @DrawableRes val activeId: Int,
    val label: Int? = null,
) {
    data object Like: UiAction(
        R.drawable.ic_love_outline,
        R.drawable.ic_love,
        R.string.like_label,
    )
    data object Dislike: UiAction(
        R.drawable.ic_hate_outline,
        R.drawable.ic_hate,
        R.string.dislike_label,
    )
    data object Comment: UiAction(
        R.drawable.ic_comment_outline,
        R.drawable.ic_comment_outline,
        R.string.comment_label,
    )
    data object View: UiAction(
        R.drawable.ic_view,
        R.drawable.ic_view,
        R.string.view_label,
    )
    companion object {
        val ENGAGEMENTS = arrayOf(Like, Dislike, Comment, View)
    }
}
