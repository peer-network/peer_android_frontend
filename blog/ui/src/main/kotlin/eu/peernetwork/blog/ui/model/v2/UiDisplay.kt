package eu.peernetwork.blog.ui.model.v2

data class UiDisplay(
    val size: String,
    val resolution: Pair<Int, Int>?,
    val cover: String? = null
)
