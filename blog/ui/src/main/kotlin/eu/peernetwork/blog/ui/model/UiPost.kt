package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList

@Stable
data class UiPost(
    val id: String,
    val title: AnnotatedString,
    val media: ImmutableList<UiMedia>,
    val author: UiAuthor,
    val type: Type,
    val aspectRatio: Float,
    val time: String,
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
        AUDIO,
        TEXT
    }
}
