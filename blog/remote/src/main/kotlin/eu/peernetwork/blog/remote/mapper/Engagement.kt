package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Engagement
import type.ActionType

fun Engagement.Content.mapToAction(): ActionType {
    return when (this) {
        Engagement.Content.Like -> ActionType.LIKE
        Engagement.Content.Dislike -> ActionType.DISLIKE
        Engagement.Content.View -> ActionType.VIEW
    }
}
