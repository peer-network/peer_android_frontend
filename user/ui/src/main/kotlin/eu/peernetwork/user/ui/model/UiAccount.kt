package eu.peernetwork.user.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class UiAccount(
    val id: String,
    val slug: Int,
    val username: String,
    val bio: AnnotatedString?,
    val imageUrl: String,
    val overview: UiOverview,
    val isfollowing: Boolean,
    val isfollowed: Boolean
)
