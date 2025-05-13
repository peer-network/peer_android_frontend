package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiPost

object PostMock {
    fun model(): UiPost {
        return UiPost(
            id = "<test-id>",
            type = "<test-type>",
            title = "<test-title>",
            description = "<test-description>",
            author = MemberMock.model()
        )
    }
}
