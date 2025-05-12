package eu.peernetwork.social.ui.renderder

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer
import eu.peernetwork.social.ui.connection.ConnectionController

interface BlogRenderer : Renderer.Stateful<BlogRenderer.Spec> {
    enum class Type {
        PHOTO,
        VIDEO,
        AUDIO,
        UNSPECIFIED,
    }

    data class Spec(
        val id: String,
        val state: MutableState<Boolean>,
        val limit: Int,
        val type: Type,
        val viewModelStoreOwner: ViewModelStoreOwner,
        val onMentionClick: (String) -> Unit = {},
        val onHashtagClick: (String) -> Unit = {},
        val imageOnClick: (String) -> Unit = {},
    )
}
