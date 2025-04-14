package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiVideo(
    val id: String,
    val title: String,
    val description: String,
    val media: String,
    val author: UiAuthor,
    val createdAt: Long,
    val likes: Int,
    val dislikes: Int,
    val comment: Int,
    val resolution: Pair<Int, Int>?
)
