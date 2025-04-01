package eu.peernetwork.blog.domain.model

sealed interface ContentType {
    data object Text : ContentType
    data class Image(val files: List<String>) : ContentType
    data class Video(val files: List<String>) : ContentType
    data class Audio(val files: List<String>, val cover: String) : ContentType
}
