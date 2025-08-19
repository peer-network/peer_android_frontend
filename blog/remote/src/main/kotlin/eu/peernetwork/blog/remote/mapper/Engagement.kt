package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Engagement
import type.GetOnly
import type.PostActionType

fun Engagement.Content.mapToAction(): PostActionType {
    return when (this) {
        Engagement.Content.Like -> PostActionType.LIKE
        Engagement.Content.Dislike -> PostActionType.DISLIKE
        Engagement.Content.View -> PostActionType.VIEW
        Engagement.Content.Report -> PostActionType.REPORT
        Engagement.Content.Save -> PostActionType.SAVE
    }
}

fun Engagement.Content.mapToInteraction(): GetOnly {
    return when (this) {
        Engagement.Content.Like -> GetOnly.LIKE
        Engagement.Content.Dislike -> GetOnly.DISLIKE
        else -> GetOnly.VIEW
    }
}
