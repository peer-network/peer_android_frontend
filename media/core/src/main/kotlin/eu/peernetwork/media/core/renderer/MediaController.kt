package eu.peernetwork.media.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import eu.peernetwork.core.ui.renderer.Renderer

interface MediaController : Renderer.Stateful<MediaController.Spec> {
    @Composable
    fun Volume()

    @Composable
    fun Progress(
        progress: MutableFloatState,
        isPlaying: State<Boolean>,
        modifier: Modifier
    )

    @Composable
    fun Content(
        path: String,
        expanded: Boolean,
        enabled: State<Boolean>,
        isPlaying: State<Boolean>,
        length: MutableLongState,
        modifier: Modifier,
        onToggle: (Boolean) -> Unit
    )

    data class Spec(
        val path: String,
        val translucent: Boolean,
    )
}
