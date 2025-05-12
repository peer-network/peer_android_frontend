package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class UiPost(
    val id: String,
    val title: AnnotatedString,
    val media: List<UiMedia>,
    val author: UiAuthor,
    val type: Type,
    val createdAt: Long,
    val description: AnnotatedString,
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
