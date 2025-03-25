package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.data.model.AccountModel
import `protected`.eu.peernetwork.user.remote.ProfileQuery

fun ProfileQuery.AffectedRows.mapToDomain(): AccountModel {
    return AccountModel(
        id = id!!,
        slug = slug!!,
        username = username!!,
        biography = biography!!,
        imageUrl = img!!,
        followed = amountfollowed!!,
        follower = amountfollower!!,
        posts = amountposts!!,
        peers = amountfriends
    )
}
