package eu.peernetwork.media.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.ui.Modifier
import eu.peernetwork.core.ui.renderer.Renderer

interface VideoPlayer : Renderer.Stateful<VideoPlayer.Spec> {
    @Composable
    fun Controller(
        modifier: Modifier,
        progress: MutableFloatState,
        length: MutableLongState
    )

    data class Spec(
        val url: String,
        val ratio: Float,
        val progress: MutableFloatState,
        val length: MutableLongState,
        val enabled: Boolean = false,
        val resolution: Pair<Int, Int>? = null,
        val volume: Float = 0f
    )
}
