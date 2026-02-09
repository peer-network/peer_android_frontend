package eu.peernetwork.social.remote.mock

import eu.peernetwork.core.remote.model.Status
import social.social.eu.peernetwork.social.remote.SearchTagsQuery

object TagMock {
    fun tags(): SearchTagsQuery.SearchTags {
        return SearchTagsQuery.SearchTags(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                SearchTagsQuery.AffectedRow(name = "<test-title>")
            )
        )
    }
}
