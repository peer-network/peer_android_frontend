package eu.peernetwork.blog.ui.post

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer

interface PostUserConnection : Renderer.Stateful<PostUserConnection.Spec> {
    @Composable
    fun Compose(
        viewModelStoreOwner: ViewModelStoreOwner,
        content: @Composable () -> Unit
    )

    data class Spec(
        val id: String,
        val isFollowing: Boolean,
        val isFollowed: Boolean
    )
}
