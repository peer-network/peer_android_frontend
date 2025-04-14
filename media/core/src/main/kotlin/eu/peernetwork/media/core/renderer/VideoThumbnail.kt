package eu.peernetwork.media.core.renderer

interface VideoThumbnail : Renderer.Stateful<VideoThumbnail.Spec> {
    data class Spec(
        val url: String,
        val resolution: Pair<Int, Int>? = null,
        val volume: Float = 0f,
    )
}
