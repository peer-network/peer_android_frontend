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
    val isViewed: Boolean,
    val dislikes: Int,
    val views: Int,
    val comment: Int,
    val url: String
) {
    enum class Type {
        IMAGE,
        AUDIO,
        TEXT,
        VIDEO
    }

    data class Detail(
        val slug: String,
        val username: String,
        val title: AnnotatedString,
        val description: AnnotatedString,
        val imageUrl: String,
        val time: String,
    )

    data class Engagement(
        val id: String,
        val likes: String,
        val dislikes: String,
        val isLiked: Boolean,
        val isDisliked: Boolean,
        val views: String,
        val comment: String
    )
}
