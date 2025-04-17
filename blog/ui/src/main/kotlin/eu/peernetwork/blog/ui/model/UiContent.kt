package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiContent(
    val id: String,
    val title: String,
    val author: UiAuthor,
    val createdAt: Long,
    val description: String,
    val likes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val dislikes: Int,
    val comment: Int
)
