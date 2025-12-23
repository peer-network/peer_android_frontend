package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.engagement.EngagementIntent
import eu.peernetwork.wallet.ui.model.UiToken

fun EngagementIntent.toUiToken(): UiToken {
    return when(this) {
        is EngagementIntent.Post -> UiToken.Posts
        is EngagementIntent.Like -> UiToken.Likes
        is EngagementIntent.DisLike -> UiToken.DisLikes
        is EngagementIntent.Comment -> UiToken.Comments
    }
}
