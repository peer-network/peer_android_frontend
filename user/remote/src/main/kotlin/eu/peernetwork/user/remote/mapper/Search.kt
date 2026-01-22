package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Status
import eu.peernetwork.user.domain.model.User
import protected.eu.peernetwork.user.remote.SearchuserQuery

fun SearchuserQuery.AffectedRow.mapToDomain(): User {
    return User(
        id = id!!,
        slug = slug!!,
        username = username ?: "",
        bio = biography ?: "",
        imageUrl = img ?: "",
        isAccessible = !isHiddenForUsers,
        status = visibilityStatus.mapToDomain()
    )
}
