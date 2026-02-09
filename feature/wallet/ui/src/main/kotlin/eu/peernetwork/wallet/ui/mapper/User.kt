package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.User
import eu.peernetwork.wallet.ui.model.UiUser

fun User.mapFromDomain(): UiUser {
    return UiUser(
        id = id,
        slug = slug,
        username = username,
        imageUrl = imageUrl,
        isAccessible = isAccessible,
        status = status.mapFromDomain()
    )
}
