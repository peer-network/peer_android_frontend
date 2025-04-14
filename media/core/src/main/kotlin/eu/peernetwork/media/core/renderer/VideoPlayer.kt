package eu.peernetwork.media.core.renderer

interface VideoPlayer : Renderer.Stateful<VideoPlayer.Spec> {
    data class Spec(
        val url: String,
        val resolution: Pair<Int, Int>? = null,
        val volume: Float = 0f,
    )
}
