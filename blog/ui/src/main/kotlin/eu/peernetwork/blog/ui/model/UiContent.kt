package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class UiContent(
    val id: String,
    val title: AnnotatedString,
    val author: UiAuthor,
    val createdAt: Long,
    val description: AnnotatedString,
    val likes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val dislikes: Int,
    val comment: Int
)
