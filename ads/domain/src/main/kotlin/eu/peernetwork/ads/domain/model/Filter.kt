package eu.peernetwork.ads.domain.model

data class Filter(
    val postId: String? = null,
    val author: String? = null,
    val sort: Sort = Sort.NEWEST,
    val criteria: Criteria? = null
) {
    sealed interface Criteria {
        data object None : Criteria

        data class Content(
            val from: String? = null,
            val to: String? = null,
            val advertisementId: String? = null
        ) : Criteria
    }
}
