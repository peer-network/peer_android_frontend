package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.domain.model.Invite
import eu.peernetwork.user.ui.model.UiInvite

fun Invite.mapFromDomain(): UiInvite {
    return UiInvite(
        id = id,
        link = link
    )
}
