package eu.peernetwork.blog.ui.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class UiVideo(
    val id: String,
    val title: String,
    val description: String,
    val media: String,
    val author: UiAuthor,
    val createdAt: Long
)
