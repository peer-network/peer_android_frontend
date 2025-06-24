package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.engagement.EngagementType
import eu.peernetwork.wallet.ui.model.UiToken

fun EngagementType.toUiToken(): UiToken {
    return when(this) {
        is EngagementType.Post -> UiToken.Post
        is EngagementType.Like -> UiToken.Like
        is EngagementType.DisLike -> UiToken.DisLike
        is EngagementType.Comment -> UiToken.Comment
    }
}
