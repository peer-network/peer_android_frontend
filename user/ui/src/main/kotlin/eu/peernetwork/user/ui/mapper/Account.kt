package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiMetric

fun Account.mapFromDomain(): UiAccount {
    return UiAccount(
        id = id,
        slug = slug,
        username = username,
        bio = bio,
        imageUrl = imageUrl,
        metric = UiMetric(
            posts = overview.posts,
            followers = overview.followers,
            followed = overview.followed,
            peers = overview.peers
        ),
        isFollowing = following,
        isFollowed = followed,
        reported = reported
    )
}
