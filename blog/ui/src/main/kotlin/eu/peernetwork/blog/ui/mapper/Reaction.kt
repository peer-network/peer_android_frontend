package eu.peernetwork.blog.ui.mapper

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.ui.model.UiReaction

fun EngagementInteractor.Reaction.mapFromDomain(): UiReaction {
    return UiReaction(
        isLiked = like,
        isDisliked = dislike,
        commented = commented
    )
}
