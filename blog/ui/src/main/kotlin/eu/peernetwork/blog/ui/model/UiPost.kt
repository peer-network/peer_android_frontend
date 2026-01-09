package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString

@Stable
data class UiPost(
    val id: String,
    val type: UiPostType,
    val author: UiAuthor,
    val title: AnnotatedString,
    val description: AnnotatedString,
    val asset: UiAsset,
    val pinnedBy: String? = null,
    val likes: Int,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val isViewed: Boolean,
    val isAccessible: Boolean,
    val reported: Boolean,
    val status: UiStatus,
    val dislikes: Int,
    val views: Int,
    val comment: Int,
    val url: String,
    val time: UiTimer,
    val createdAt: Long,
)
