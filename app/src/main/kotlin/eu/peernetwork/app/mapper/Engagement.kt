package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.engagement.EngagementIntent
import eu.peernetwork.wallet.ui.model.UiToken

fun EngagementIntent.toUiToken(): UiToken {
    return when(this) {
        is EngagementIntent.Post -> UiToken.Post
        is EngagementIntent.Like -> UiToken.Like
        is EngagementIntent.DisLike -> UiToken.DisLike
        is EngagementIntent.Comment -> UiToken.Comment
    }
}
