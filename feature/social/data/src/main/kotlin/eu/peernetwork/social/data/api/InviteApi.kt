package eu.peernetwork.social.data.api

import eu.peernetwork.social.domain.model.Invite

interface InviteApi {
    suspend fun get(): Invite
}