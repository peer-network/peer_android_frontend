package eu.peernetwork.social.ui.mapper

import eu.peernetwork.social.domain.model.Invite
import eu.peernetwork.social.ui.model.UiInvite

fun Invite.mapFromDomain(): UiInvite {
    return UiInvite(
        id = id,
        link = link
    )
}