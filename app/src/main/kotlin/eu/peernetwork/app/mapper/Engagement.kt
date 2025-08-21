package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.engagement.EngagementEvent
import eu.peernetwork.wallet.ui.model.UiToken

fun EngagementEvent.toUiToken(): UiToken {
    return when(this) {
        is EngagementEvent.Post -> UiToken.Post
        is EngagementEvent.Like -> UiToken.Like
        is EngagementEvent.DisLike -> UiToken.DisLike
        is EngagementEvent.Comment -> UiToken.Comment
    }
}
