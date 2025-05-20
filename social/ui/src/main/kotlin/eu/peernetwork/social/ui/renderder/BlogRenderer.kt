package eu.peernetwork.social.ui.renderder

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer

interface BlogRenderer : Renderer.Stateful<BlogRenderer.Spec> {
    enum class Type {
        PHOTO,
        VIDEO,
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
        val photoState: LazyListState,
        val videoState: LazyListState
    )
}
