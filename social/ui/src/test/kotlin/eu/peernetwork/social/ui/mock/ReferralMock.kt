package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiReferral

object ReferralMock {
    fun model(): UiReferral{
        return UiReferral(
            id = "<test-id>",
            username = "<test-username>",
            slug = "<test-slug>",
            img = "<test-image-url>",
            isFollowed = true,
            isFollowing = true
        )
    }
}