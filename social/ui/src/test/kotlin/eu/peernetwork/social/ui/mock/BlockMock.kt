package eu.peernetwork.social.ui.mock

import eu.peernetwork.social.ui.model.UiBlock

object BlockMock {
    fun model(): UiBlock {
        return UiBlock(
            userId = "<test-id>",
            username = "<test-username>",
            slug = 1,
            image = "<test-image-url>"
        )
    }
}