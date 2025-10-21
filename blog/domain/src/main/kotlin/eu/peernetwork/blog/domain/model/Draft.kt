package eu.peernetwork.blog.domain.model

data class Draft(
    val title: String,
    val description: String,
    val tags: List<String>,
    val type: Type
) {
    data class Media(
        val url: String,
        val cover: String? = null
    )

    sealed interface Type {
        data class Text(val files: List<String>) : Type
        data class Image(val files: List<String>) : Type
        data class Video(val media: List<Media>) : Type
        data class Audio(val media: List<Media>) : Type
    }
}
