package eu.peernetwork.blog.domain.model

data class Draft(
    val title: String,
    val description: String,
    val tags: List<String>,
    val type: ContentType
)
