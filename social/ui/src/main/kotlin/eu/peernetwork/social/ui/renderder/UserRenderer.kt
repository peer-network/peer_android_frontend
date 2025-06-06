package eu.peernetwork.social.ui.renderder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer

interface UserRenderer : Renderer.Stateful<UserRenderer.Spec> {
    data class Spec(
        val id: String,
        val lastUpdated: State<Long>,
        val viewModelStoreOwner: ViewModelStoreOwner,
        val onSettings: () -> Unit,
        val onFollow: @Composable (Pair<Boolean, Boolean>) -> Unit,
        val onClick: (Int) -> Unit,
    )
}
