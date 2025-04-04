package eu.peernetwork.blog.ui.model

data class UiMedia(
    val path: String,
    val options: Options
) {
    data class Options(
        val size: String,
        val duration: String,
        val ratio: String,
        val resolution: String
    )
}
