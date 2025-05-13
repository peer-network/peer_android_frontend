package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.ui.model.UiMember

fun Member.mapFromDomain(): UiMember {
    return UiMember(
        id = id,
        username = username,
        imageUrl = imageUrl
    )
}
