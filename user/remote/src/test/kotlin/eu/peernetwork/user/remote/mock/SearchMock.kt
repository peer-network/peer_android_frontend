package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import protected.eu.peernetwork.user.remote.SearchuserQuery

object SearchMock {
    fun users(): SearchuserQuery.Searchuser {
        return SearchuserQuery.Searchuser(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                SearchuserQuery.AffectedRow(
                    id = "<test-id>",
                    username = "<test-username>",
                    slug = System.currentTimeMillis().toInt(),
                    img = "<test-img>",
                    biography = "<test-biography>"
                )
            )
        )
    }
}
