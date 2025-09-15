package eu.peernetwork.blog.domain.model

data class Filter(
    val type: Set<Content.Type> = emptySet(),
    val postId: String? = null,
    val author: String? = null,
    val criteria: Criteria? = null
) {
    sealed class Criteria(
        val sort: Sort = Sort.NEW,
        val tag: String? = null,
        val title: String? = null,
    ) {
        data object Default : Criteria()

        data class Content(
            val sortBy: Sort = Sort.NEW,
            val tagFilter: String? = null,
            val titleFilter: String? = null
        ) : Criteria(sortBy, tagFilter, titleFilter)

        data class Reaction(
            val engagement: Engagement,
            val sortBy: Sort = Sort.NEW,
            val tagFilter: String? = null,
            val titleFilter: String? = null
        ) : Criteria(sortBy, tagFilter, titleFilter)
    }
}
