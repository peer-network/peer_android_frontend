package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Referral
import eu.peernetwork.social.ui.model.UiReferral

fun Referral.mapFromDomain(): UiReferral {
    return UiReferral(
        id = id,
        username = username,
        slug = slug,
        img = img,
        isFollowed = isFollowed,
        isFollowing = isFollowing
    )
}
