package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiPost

object PostMock {
    fun model(): UiPost {
        return UiPost(
            id = "<test-id>",
            title = "<test-title>",
            description = "<test-description>",
            author = MemberMock.model()
        )
    }
}
