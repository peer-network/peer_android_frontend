package eu.peernetwork.user.data.mapper

import eu.peernetwork.user.data.model.AccountModel
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.Overview

fun AccountModel.mapToDomain(): Account {
    return Account(
        id = id,
        slug = slug,
        username = username,
        bio = biography,
        imageUrl = imageUrl,
        overview = Overview(
            posts = posts,
            followed = followed,
            followers = follower,
            peers = peers
        ),
        followed = isfollowed,
        following = isfollowing,
        reported = hasActiveReports
    )
}
