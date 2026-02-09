package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.model.UiStatus

object MemberMock {
    fun model(): UiMember {
        return UiMember(
            id = "<test-id>",
            slug = "<test-slug>",
            username = "<test-username>",
            imageUrl = "<test-image-url>",
            isFollowed = true,
            isFollowing = true,
            isAccessible = true,
            status = UiStatus.VISIBLE
        )
    }
}
