package eu.peernetwork.blog.ui.post

import androidx.compose.runtime.staticCompositionLocalOf
import eu.peernetwork.blog.ui.post.Post.Component

interface PostInteractor {
    fun component(): Component

    companion object {
        val LocalPostInteractor = staticCompositionLocalOf<PostInteractor> {
            error("PostInteractor not provided")
        }
    }
}
