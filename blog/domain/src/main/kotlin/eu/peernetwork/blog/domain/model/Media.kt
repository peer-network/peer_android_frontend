package eu.peernetwork.blog.domain.model

data class Media(
    val path: String,
    val options: Options
) {
    data class Options(
        val size: String,
        val cover: String? = null,
        val resolution: Pair<Int, Int>? = null,
    )
}
