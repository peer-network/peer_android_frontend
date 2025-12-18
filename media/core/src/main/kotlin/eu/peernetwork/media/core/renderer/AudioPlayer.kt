package eu.peernetwork.media.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import eu.peernetwork.core.ui.renderer.Renderer

interface AudioPlayer : Renderer.Stateful<AudioPlayer.Spec> {
    @Composable
    fun Thumbnail(
        path: String,
        hasControls: Boolean,
        enable: State<Boolean>,
        isPlaying: State<Boolean>,
        length: MutableLongState,
        modifier: Modifier,
        onPlay: (Boolean) -> Unit
    )

    @Composable
    fun Volume()

    data class Spec(
        val path: String,
        val progress: MutableFloatState,
        val length: MutableLongState,
        val enabled: State<Boolean>,
        val position: Int,
        val modifier: Modifier
    )
}
