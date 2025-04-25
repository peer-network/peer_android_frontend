package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiPost(
    val id: String,
    val title: String,
    val media: List<UiMedia>,
    val author: UiAuthor,
    val type: Type,
    val createdAt: Long,
    val description: String,
    val likes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val dislikes: Int,
    val comment: Int
) {
    enum class Type {
        IMAGE,
        TEXT
    }
}
