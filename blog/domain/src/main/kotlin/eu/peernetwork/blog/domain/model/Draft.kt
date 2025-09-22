package eu.peernetwork.blog.domain.model

data class Draft(
    val title: String,
    val description: String,
    val tags: List<String>,
    val type: Type
) {
    sealed interface Type {
        data class Text(val files: List<String>) : Type
        data class Image(val files: List<String>) : Type
        data class Video(val files: List<String>) : Type
        data class Audio(val files: List<String>, val cover: String? = null) : Type
    }
}
