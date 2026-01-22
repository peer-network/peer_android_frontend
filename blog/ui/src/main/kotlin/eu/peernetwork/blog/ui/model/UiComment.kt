package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class UiComment (
    val id: String,
    val author: UiAuthor,
    val content: AnnotatedString,
    val createdAt: Long,
    val likes: Int,
    val isLiked: Boolean,
    val isReported: Boolean,
    val isAccessible: Boolean,
    val status: UiStatus,
)
