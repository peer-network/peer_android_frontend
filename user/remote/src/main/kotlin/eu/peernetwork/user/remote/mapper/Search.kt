package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Account
import protected.eu.peernetwork.user.remote.SearchuserQuery

fun SearchuserQuery.AffectedRow.mapToDomain(): Account {
    return Account(
        id = id!!,
        slug = slug!!,
        username = username ?: "",
        bio = biography ?: "",
        imageUrl = img ?: "",
    )
}
