package eu.peernetwork.social.domain.repository

import eu.peernetwork.social.domain.model.Invite

interface InviteRepository {
    suspend fun get(): Invite
}