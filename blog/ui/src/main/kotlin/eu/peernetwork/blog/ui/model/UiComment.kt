package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiComment (
    val id: String,
    val author: UiAuthor,
    val content: String,
    val createdAt: Long,
    val likes: Int,
    val isLiked: Boolean
)
