package eu.peernetwork.media.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import eu.peernetwork.core.ui.renderer.Renderer

interface MediaController : Renderer.Stateful<MediaController.Spec> {
    @Composable
    fun Volume()

    data class Spec(
        val path: String,
        val enabled: State<Boolean>
    )
}
