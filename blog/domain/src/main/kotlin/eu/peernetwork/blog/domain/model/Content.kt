package eu.peernetwork.blog.domain.model

data class Content(
    val id: String,
    val title: String,
    val media: String,
    val author: Author,
    val type: Type,
    val createdAt: Long
) {
    enum class Type {
        IMAGE,
        VIDEO,
        AUDIO,
        TEXT,
    }
}
