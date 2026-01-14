package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString

@Stable
data class UiPostDetail(
    val id: String,
    val slug: String,
    val username: String,
    val title: AnnotatedString,
    val description: AnnotatedString,
    val imageUrl: String,
    val time: UiTimer,
    val reported: Boolean,
    val isAccessible: Boolean,
    val pinnedBy: String? = null,
)
