package eu.peernetwork.blog.ui.engagement

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.renderer.Renderer

interface EngagementDialog : Renderer.Stateful<EngagementDialog.Spec> {
    data class Spec(
        val type: MutableState<EngagementIntent?>,
        val viewModelStoreOwner: ViewModelStoreOwner,
        val onConfirm: (EngagementIntent) -> Unit
    )
}
