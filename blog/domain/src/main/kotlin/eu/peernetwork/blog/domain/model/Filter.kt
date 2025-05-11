package eu.peernetwork.blog.domain.model

data class Filter(
    val type: Set<Content.Type> = emptySet(),
    val postId: String? = null,
    val author: String? = null,
    val criteria: Criteria? = null,
) {
    sealed interface Criteria {
        data class Content(
            val sort: Sort = Sort.NEW,
            val tag: String? = null,
            val title: String? = null,
        ) : Criteria
        data class Reaction(val engagement: Engagement) : Criteria
    }
}
