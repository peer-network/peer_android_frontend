package eu.peernetwork.blog.ui.model.v2

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString

@Stable
data class UiPostDetail(
    val slug: String,
    val username: String,
    val title: AnnotatedString,
    val description: AnnotatedString,
    val imageUrl: String,
    val time: UiTimer,
    val pinnedBy: String? = null,
)
