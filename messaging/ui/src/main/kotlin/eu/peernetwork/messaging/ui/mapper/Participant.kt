package eu.peernetwork.messaging.ui.mapper

import eu.peernetwork.messaging.domain.model.Participant
import eu.peernetwork.messaging.ui.model.UiParticipant

fun Participant.mapFromDomain(): UiParticipant {
    return UiParticipant(userId, username, slug, image)
}