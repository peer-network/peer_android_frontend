package eu.peernetwork.media.core.renderer

import eu.peernetwork.core.ui.renderer.Renderer

interface VideoPlayer : Renderer.Stateful<VideoPlayer.Spec> {
    data class Spec(
        val url: String,
        val resolution: Pair<Int, Int>? = null,
        val volume: Float = 0f,
    )
}
