package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiMember

object MemberMock {
    fun model(): UiMember {
        return UiMember(
            id = "<test-id>",
            username = "<test-username>",
            imageUrl = "<test-image-url>"
        )
    }
}
