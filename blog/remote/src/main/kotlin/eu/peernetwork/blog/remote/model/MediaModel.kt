package eu.peernetwork.blog.remote.model

data class MediaModel(
    val path: String,
    val options: Options?
) {
    data class Options(
        val size: String?,
        val cover: String? = null,
        val resolution: String? = null,
    )
}
