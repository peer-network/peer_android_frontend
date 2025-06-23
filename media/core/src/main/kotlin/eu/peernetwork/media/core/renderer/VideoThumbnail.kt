package eu.peernetwork.media.core.renderer

import eu.peernetwork.core.ui.renderer.Renderer

interface VideoThumbnail : Renderer.Stateful<VideoThumbnail.Spec> {
    data class Spec(
        val url: String,
        val ratio: Float,
        val resolution: Pair<Int, Int>? = null,
        val volume: Float = 0f,
    )
}
