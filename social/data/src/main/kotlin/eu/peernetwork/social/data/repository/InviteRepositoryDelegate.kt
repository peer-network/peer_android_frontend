package eu.peernetwork.social.data.repository

import eu.peernetwork.social.data.api.InviteApi
import eu.peernetwork.social.domain.model.Invite
import eu.peernetwork.social.domain.repository.InviteRepository
import javax.inject.Inject

class InviteRepositoryDelegate @Inject constructor(
    private val api: InviteApi
): InviteRepository {
    override suspend fun get(): Invite {
        return api.get()
    }
}