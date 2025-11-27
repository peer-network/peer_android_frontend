package eu.peernetwork.blog.ui.model.v2

import androidx.compose.runtime.Immutable

@Immutable
data class UiAuthor(
    val id: String,
    val slug: Int,
    val username: String,
    val imageUrl: String,
    val following: Boolean,
    val followed: Boolean
)
