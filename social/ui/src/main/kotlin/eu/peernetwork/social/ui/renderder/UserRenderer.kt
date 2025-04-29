package eu.peernetwork.social.ui.renderder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer

interface UserRenderer : Renderer.Stateful<UserRenderer.Spec> {
    enum class Type {
        USER,
        ACCOUNT
    }
    data class Spec(
        val id: String,
        val state: MutableState<Boolean>,
        val viewModelStoreOwner: ViewModelStoreOwner,
        val type: Type,
        val onSettings: () -> Unit,
        val onFollow: @Composable (Boolean) -> Unit,
        val onClick: (Int) -> Unit,
    )
}
