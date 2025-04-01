package eu.peernetwork.blog.domain.model

data class Filter(
    val type: Set<ContentType> = emptySet(),
    val postId: String? = null,
    val criteria: Criteria? = null,
) {
    sealed interface Criteria {
        data class Content(val sort: Sort = Sort.NEW) : Criteria
        data class Reaction(val engagement: Engagement) : Criteria
    }
}
