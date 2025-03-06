package eu.peernetwork.user.remote.mock

import protected.eu.peernetwork.user.remote.ProfileQuery

object ProfileMock {
    fun profile(): ProfileQuery.AffectedRows {
        return ProfileQuery.AffectedRows(
            id = "<test-id>",
            username = "<test-username>",
            slug = 37958,
            img = "<test-img>",
            biography = "<test-biography>"
        )
    }
}
