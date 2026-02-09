package eu.peernetwork.blog.ui.post

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import eu.peernetwork.blog.ui.post.Post.Component

interface PostInteractor {
    fun observe(): State<Map<String, Bitmap?>>

    fun component(): Component

    fun background(
        media: String,
        aspectRatio: Float,
        width: Int,
        height: Int = width,
        fit: Boolean = false
    )

    companion object {
        val LocalPostInteractor = staticCompositionLocalOf<PostInteractor> {
            error("PostInteractor not provided")
        }
    }
}
