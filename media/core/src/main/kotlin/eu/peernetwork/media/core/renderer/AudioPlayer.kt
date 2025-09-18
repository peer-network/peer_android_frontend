package eu.peernetwork.media.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import eu.peernetwork.core.ui.renderer.Renderer

interface AudioPlayer : Renderer.Stateful<AudioPlayer.Spec> {
    @Composable
    fun Thumbnail(
        path: String,
        position: Int,
        hasControls: Boolean,
        isActive: State<Boolean>,
        enable: State<Boolean>,
        length: MutableLongState,
        current: MutableState<Int>,
        modifier: Modifier
    )

    data class Spec(
        val path: String,
        val progress: MutableFloatState,
        val length: MutableLongState,
        val enabled: Boolean,
        val position: Int,
        val current: MutableState<Int>,
        val modifier: Modifier
    )
}
