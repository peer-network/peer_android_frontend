package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class UiVideo(
    val id: String,
    val title: AnnotatedString,
    val description: AnnotatedString,
    val media: String,
    val author: UiAuthor,
    val time: String,
    val createdAt: Long,
    val likes: Int,
    val dislikes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val comment: Int,
    val aspectRatio: Float,
    val resolution: Pair<Int, Int>?
)
