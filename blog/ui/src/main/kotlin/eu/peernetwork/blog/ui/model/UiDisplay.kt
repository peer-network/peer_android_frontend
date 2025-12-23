package eu.peernetwork.blog.ui.model

data class UiDisplay(
    val size: String,
    val resolution: Pair<Int, Int>?,
    val cover: String? = null
)
